package com.jwtauth.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwtauth.service.EmployeeReportService;

@RestController
@RequestMapping(value = "/auth")
public class EmployeeReportsController {

	@Autowired
	EmployeeReportService service;

	@GetMapping("/empoyees-report")
	public ResponseEntity<InputStreamResource> getEmployeesReport() throws FileNotFoundException {
		File file = new File(service.generateEmployeeDetailsReports());
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		return ResponseEntity.ok().contentLength(file.length())
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
	}

}
