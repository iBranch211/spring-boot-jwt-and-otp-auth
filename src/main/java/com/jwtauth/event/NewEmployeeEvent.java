package com.jwtauth.event;

import org.springframework.context.ApplicationEvent;

import com.jwtauth.entity.Employee;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
	public class NewEmployeeEvent extends ApplicationEvent {

	private static final long serialVersionUID = -6988515637931141597L;

	private Employee newEmployee;

	public NewEmployeeEvent(Object source, Employee newEmployee) {
		super(source);
		this.newEmployee = newEmployee;
	}

}
