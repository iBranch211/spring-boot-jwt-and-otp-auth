package com.jwtauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jwtauth.entiry.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
