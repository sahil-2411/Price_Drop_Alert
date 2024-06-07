package com.pricedrop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PriceDropAlertApplication {

	public static void main(String[] args) {
		SpringApplication.run(PriceDropAlertApplication.class, args);

	}

}
