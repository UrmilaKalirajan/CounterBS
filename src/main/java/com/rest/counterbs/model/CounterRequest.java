package com.rest.counterbs.model;

import java.util.List;

public class CounterRequest {
	
	private List<String> searchText;

	@Override
	public String toString() {
		return "CounterRequest [searchText=" + searchText + "]";
	}

	public List<String> getSearchText() {
		return searchText;
	}

	public void setSearchText(List<String> searchText) {
		this.searchText = searchText;
	}

}
