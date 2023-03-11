package com.jwtauth.service;

import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jwtauth.entity.Employee;
import com.jwtauth.repository.EmployeeRepository;

@Service
public class EmployeeBasicDataReportGenerator extends ReportGenerator {

	@Autowired
	EmployeeRepository empRepo;

	@Override
	public String getSheetName() {
		return "Employees";
	}

	@Override
	public List<String> getColumnNames() {
		return Arrays.asList("First Name", "Last Name", "Job", "Salary", "Email");
	}

	@Override
	public void fillActualData(Sheet sheet) {
		List<Employee> allEmployees = empRepo.findAll();

		int rowNumber = 1;

		for (Employee employee : allEmployees) {
			Row row = sheet.createRow(rowNumber++);
			Cell cell = row.createCell(0);
			cell.setCellValue(employee.getFirstName());

			cell = row.createCell(1);
			cell.setCellValue(employee.getLastName());

			cell = row.createCell(2);
			cell.setCellValue(employee.getJob());

			cell = row.createCell(3);
			cell.setCellValue(employee.getSalary());

			cell = row.createCell(4);
			cell.setCellValue(employee.getEmail());
		}

	}

	@Override
	public String getFileName() {
		return "Employee_Details";
	}

}
