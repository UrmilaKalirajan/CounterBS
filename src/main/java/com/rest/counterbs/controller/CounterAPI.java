package com.rest.counterbs.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rest.counterbs.model.CounterRequest;
import com.rest.counterbs.model.CounterResponse;
import com.rest.counterbs.service.CounterService;

@RestController
@RequestMapping(path = "/counter-api")
public class CounterAPI {

	@Autowired
	private CounterService counterService;

	@PostMapping(path = "/search", consumes = "application/json", produces = "application/json")
	public ResponseEntity<CounterResponse> addEmployee(@RequestBody CounterRequest counterRequest) {

		String input = counterService.getInput();

		String textLower = input.toLowerCase();
		textLower = textLower.replaceAll("\\W", " ");
		textLower = textLower.replaceAll("\\s+", " ");
		String[] words = textLower.split("\\s+");

		Set<String> noDup = new LinkedHashSet<String>(Arrays.asList(words));
		String[] noDupWords = new String[noDup.size()];
		noDupWords = noDup.toArray(noDupWords);

		// String retText = "";
		Map<String, Integer> responseMap = new HashMap<String, Integer>();
		if (counterRequest != null && !CollectionUtils.isEmpty(counterRequest.getSearchText())) {
			List<String> searchStringList = counterRequest.getSearchText();
			for (String searchString : searchStringList) {
				int i = 0;
				int count = 0;
				for (int j = 0; j < words.length; j++) {
					if (searchString.equals(words[j])) {
						count = count + 1;
					}
				}
				i++;
				responseMap.put(searchString, count);
				// retText = retText +noDupWords[i]+","+count+"\n";
			}
		}
		CounterResponse counterResponse = new CounterResponse();
		counterResponse.setCounts(responseMap);
		return ResponseEntity.status(HttpStatus.OK).body(counterResponse);
	}

}
