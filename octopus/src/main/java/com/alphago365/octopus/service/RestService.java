package com.alphago365.octopus.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class RestService {

    private final RestTemplate restTemplate;

    public RestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getJson(String url) {
        return getJson(url, null);
    }

    public String getJson(String url, Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        // default
        httpHeaders.set("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.116 Safari/537.36");

        // custom
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(httpHeaders::set);
        }
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), String.class);
        logResponseDetails(responseEntity);
        return responseEntity.getBody();
    }

    private void logResponseDetails(ResponseEntity<?> response) {
        log.info("Headers: {}", response.getHeaders());
        log.debug("Response status: {}", response.getStatusCode().toString());
        log.debug("Response body: {}", response.getBody());
    }
}