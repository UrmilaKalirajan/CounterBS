package com.rest.counterbs.model;

import java.util.Map;

public class CounterResponse {
	
	private Map<String,Integer> counts;

	public Map<String, Integer> getCounts() {
		return counts;
	}

	public void setCounts(Map<String, Integer> counts) {
		this.counts = counts;
	}

}
