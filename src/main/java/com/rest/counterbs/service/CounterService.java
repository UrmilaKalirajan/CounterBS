package com.rest.counterbs.service;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CounterService {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Value("${proxy.uri}")
	private String uri;

	@Value("${proxy.creds}")
	private String creds;

	public String getInput() {

		byte[] plainCredsBytes = creds.getBytes();
		byte[] base64CredsBytes = Base64.getEncoder().encode(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);

		HttpEntity request = new HttpEntity(headers);

		ResponseEntity<String> response = new RestTemplate().exchange(uri, HttpMethod.GET, request, String.class);

		String result = response.getBody();

		return result;
	}

}
