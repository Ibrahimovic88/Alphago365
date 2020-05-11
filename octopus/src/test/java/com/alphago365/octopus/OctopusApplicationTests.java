package com.alphago365.octopus;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@SpringBootTest
class OctopusApplicationTests {

	@Test
	void contextLoads() {
		String strInstantTime = "2020-05-10T07:30:00Z";
		ZonedDateTime zonedDateTime = Instant.parse(strInstantTime).atZone(ZoneId.systemDefault());
		System.out.println(zonedDateTime);
		System.out.println(zonedDateTime.toLocalDateTime());
	}

}
