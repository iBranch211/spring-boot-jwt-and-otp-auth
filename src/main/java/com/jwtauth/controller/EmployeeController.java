package com.jwtauth.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jwtauth.dto.EmployeeDTO;
import com.jwtauth.entity.Employee;
import com.jwtauth.event.NewEmployeeEvent;
import com.jwtauth.exception.EmployeeNotFoundException;
import com.jwtauth.mapper.EmployeeMapper;
import com.jwtauth.repository.EmployeeRepository;

@RestController
@RequestMapping(value = "/protected")
@Validated
public class EmployeeController {

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@GetMapping(value = "/employees")
	List<Employee> findAll() {
		return employeeRepository.findAll();
	}

	@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
	@GetMapping(value = "/employee/{id}")
	ResponseEntity<Employee> findById(@PathVariable("id") @Min(1) int id) {
		Employee emp = employeeRepository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee with ID :" + id));
		return ResponseEntity.ok().body(emp);
	}

	@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
	@PostMapping(value = "/employees")
	ResponseEntity<?> createEmployee(@Valid @RequestBody EmployeeDTO empdto) {
		Employee emp = EmployeeMapper.dtoToEntity(empdto);
		Employee addedemp = employeeRepository.save(emp);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(addedemp.getId())
				.toUri();

		NewEmployeeEvent employeeEvent = new NewEmployeeEvent(this, addedemp);
		applicationEventPublisher.publishEvent(employeeEvent);

		return ResponseEntity.created(location).build();
	}

	@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
	@PutMapping(value = "/employee/{id}")
	ResponseEntity<Employee> updateEmployee(@PathVariable("id") @Min(1) int id,
			@Valid @RequestBody EmployeeDTO empdto) {
		Employee emp = employeeRepository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee with ID :" + id));
		Employee empu = EmployeeMapper.dtoToEntity(empdto);
		empu.setId(emp.getId());
		employeeRepository.save(empu);
		return ResponseEntity.ok().body(empu);
	}

	@PreAuthorize("hasAuthority('ROLE_MANAGER')")
	@DeleteMapping(value = "/employee/{id}")
	ResponseEntity<String> deleteEmployee(@PathVariable("id") @Min(1) int id) {
		Employee emp = employeeRepository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee Not Found with ID :" + id));
		employeeRepository.deleteById(emp.getId());
		return ResponseEntity.ok().body("Employee deleted with success!");
	}
}