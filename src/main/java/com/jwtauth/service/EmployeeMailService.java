package com.jwtauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.jwtauth.entity.Employee;

@Service
public class EmployeeMailService {

	@Autowired
	MailService mailService;

	public void sendEmployeeWelcomeMail(Employee emp) {
		final Context ctx = new Context();
		ctx.setVariable("name", emp.getFirstName());

		mailService.sendHTMLMail(ctx, "welcome-employee", emp.getEmail(), "Welcome to Organization");
	}

}
