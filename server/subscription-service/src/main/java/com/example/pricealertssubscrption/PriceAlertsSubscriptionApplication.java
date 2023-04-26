package com.example.pricealertssubscrption;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class PriceAlertsSubscriptionApplication {

	public static void main(String[] args) {
		SpringApplication.run(PriceAlertsSubscriptionApplication.class, args);
	}

}
