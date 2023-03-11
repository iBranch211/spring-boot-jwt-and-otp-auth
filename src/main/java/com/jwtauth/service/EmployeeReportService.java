package com.jwtauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeReportService {

	@Autowired
	EmployeeBasicDataReportGenerator basicDataReportGenerator;

	public String generateEmployeeDetailsReports() {
		return basicDataReportGenerator.generateReport();
	}

}
