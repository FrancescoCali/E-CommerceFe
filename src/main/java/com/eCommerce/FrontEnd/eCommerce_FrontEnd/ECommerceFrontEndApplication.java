package com.eCommerce.FrontEnd.eCommerce_FrontEnd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ECommerceFrontEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommerceFrontEndApplication.class, args);
	}

	@Bean
	public RestTemplate rest(){
		RestTemplate t = new RestTemplate();
		return t;
	}
}
