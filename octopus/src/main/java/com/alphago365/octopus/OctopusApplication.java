package com.alphago365.octopus;

import com.alphago365.octopus.config.DownloadConfig;
import com.alphago365.octopus.config.JobConfig;
import com.alphago365.octopus.config.JwtConfig;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({JwtConfig.class, JobConfig.class, DownloadConfig.class})
@EnableScheduling
public class OctopusApplication {

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(OctopusApplication.class, args);
    }

    public static void restart() {
        ApplicationArguments args = context.getBean(ApplicationArguments.class);
        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(OctopusApplication.class, args.getSourceArgs());
        });
        thread.setDaemon(false);
        thread.start();
    }

    @Bean
    public CustomRestTemplateCustomizer customRestTemplateCustomizer() {
        return new CustomRestTemplateCustomizer();
    }

}
