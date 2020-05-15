package com.alphago365.octopus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.*;

@Slf4j
public class CustomRestTemplateCustomizer implements RestTemplateCustomizer {

    @Override
    public void customize(RestTemplate restTemplate) {
        restTemplate.getInterceptors().add(new CustomHttpRequestInterceptor());
    }

    private static class CustomHttpRequestInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(
                HttpRequest request, byte[] body,
                ClientHttpRequestExecution execution) throws IOException {

            logRequestDetails(request);
            return execution.execute(request, body);
        }

        private void logRequestDetails(HttpRequest request) {
            log.debug("Headers: {}", request.getHeaders());
            log.debug("Request Method: {}", request.getMethod());
            log.debug("Request URI: {}", request.getURI());
        }

    }
}