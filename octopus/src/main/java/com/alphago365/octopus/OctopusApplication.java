package com.alphago365.octopus;

import com.alphago365.octopus.config.JobConfig;
import com.alphago365.octopus.config.JwtConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtConfig.class, JobConfig.class})
public class OctopusApplication {

	public static void main(String[] args) {
		SpringApplication.run(OctopusApplication.class, args);
	}

}
