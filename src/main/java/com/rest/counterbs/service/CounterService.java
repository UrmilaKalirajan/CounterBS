package com.rest.counterbs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CounterService {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public String getInput() {
		final String uri = "http://localhost:8080/springrestexample/employees.xml";

		RestTemplate restTemplate = restTemplate();
		String result = restTemplate.getForObject(uri, String.class);

		return result;
	}

}
