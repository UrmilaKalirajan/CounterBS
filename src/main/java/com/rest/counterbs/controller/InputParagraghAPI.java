package com.rest.counterbs.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/input-api")
public class InputParagraghAPI {

	@GetMapping("/paragragh")
	ResponseEntity<String> getInputPara() {

		try// (BufferedReader br = new BufferedReader(new FileReader(new
			// File("src/main/resources/imput_para.txt"))))
		{
			String text = new String(Files.readAllBytes(Paths.get("src/main/resources/imput_para.txt")),
					StandardCharsets.UTF_8);
			return ResponseEntity.status(HttpStatus.OK).body(text);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
