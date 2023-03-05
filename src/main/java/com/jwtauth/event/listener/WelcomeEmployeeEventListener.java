package com.jwtauth.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.jwtauth.event.NewEmployeeEvent;
import com.jwtauth.service.EmployeeMailService;

@Component
public class WelcomeEmployeeEventListener implements ApplicationListener<NewEmployeeEvent> {

	@Autowired
	private EmployeeMailService employeeMailService;

	@Override
	public void onApplicationEvent(NewEmployeeEvent event) {
		employeeMailService.sendEmployeeWelcomeMail(event.getNewEmployee());
	}

}