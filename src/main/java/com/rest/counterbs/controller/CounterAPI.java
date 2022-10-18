package com.rest.counterbs.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	Logger log = LoggerFactory.getLogger(CounterAPI.class);

	@PostMapping(path = "/search", consumes = "application/json", produces = "application/json")
	public ResponseEntity<CounterResponse> getCount(@RequestBody CounterRequest counterRequest) {

		String input = counterService.getInput();

		log.debug("Input :: " + input);
		String textLower = input.toLowerCase();
		textLower = textLower.replaceAll("\\W", " ");
		textLower = textLower.replaceAll("\\s+", " ");
		String[] words = textLower.split("\\s+");

		Set<String> noDup = new LinkedHashSet<String>(Arrays.asList(words));
		String[] noDupWords = new String[noDup.size()];
		noDupWords = noDup.toArray(noDupWords);

		Map<String, Integer> responseMap = new HashMap<String, Integer>();
		if (counterRequest != null && !CollectionUtils.isEmpty(counterRequest.getSearchText())) {
			List<String> searchStringList = counterRequest.getSearchText();
			for (String searchString : searchStringList) {

				log.debug("searchString :: " + searchString);
				int count = 0;
				for (int j = 0; j < words.length; j++) {

					if (searchString.toLowerCase().equals(words[j])) {
						count = count + 1;
						log.debug("matched :: " + count);
					}
				}

				responseMap.put(searchString, count);
			}
		}
		CounterResponse counterResponse = new CounterResponse();
		counterResponse.setCounts(responseMap);
		return ResponseEntity.status(HttpStatus.OK).body(counterResponse);
	}

	@GetMapping(path = "/top/{id}")
	public ResponseEntity<Resource> getTopCount(@PathVariable Integer id) {

		LinkedHashMap<String, Integer> countByWordSorted = getCount();
		ByteArrayInputStream byteArrayOutputStream;

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();

				CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), CSVFormat.DEFAULT);) {
			int i = 1;
			for (Map.Entry<String, Integer> entry : countByWordSorted.entrySet()) {

				List<String> response = new ArrayList<String>();
				response.add(entry.getKey());
				response.add(entry.getValue().toString());
				csvPrinter.printRecord(response);
				if (i == id) {
					break;
				}
				i++;
			}

			csvPrinter.flush();
			byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}

		InputStreamResource fileInputStream = new InputStreamResource(byteArrayOutputStream);

		String csvFileName = "topcount.csv";

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + csvFileName);
		headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");
		return new ResponseEntity<>(fileInputStream, headers, HttpStatus.OK);
	}

	private LinkedHashMap<String, Integer> getCount() {
		String input = counterService.getInput();

		log.debug("Input :: " + input);
		String textLower = input.toLowerCase();
		textLower = textLower.replaceAll("\\W", " ");
		textLower = textLower.replaceAll("\\s+", " ");
		String[] words = textLower.split("\\s+");

		Set<String> noDup = new LinkedHashSet<String>(Arrays.asList(words));
		String[] noDupWords = new String[noDup.size()];
		noDupWords = noDup.toArray(noDupWords);

		Map<String, Integer> responseMap = new HashMap<String, Integer>();
		for (int i = 0; i < noDupWords.length; i++) {

			int count = 0;
			for (int j = 0; j < words.length; j++) {

				if (noDupWords[i].equals(words[j])) {
					count = count + 1;
					log.debug("matched :: " + count);
				}
			}

			responseMap.put(noDupWords[i], count);
			log.debug("responseMap :: " + noDupWords[i] + "::" + count);

		}
		LinkedHashMap<String, Integer> countByWordSorted = responseMap.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> {
					throw new IllegalStateException();
				}, LinkedHashMap::new));
		return countByWordSorted;
	}

}
