package com.employee.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.employee.domain.Employee;
import com.employee.repository.EmployeeRepo;

@Component
public class MyUserDetailsService implements UserDetailsService{
	@Autowired
	EmployeeRepo employeeRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Employee employee = employeeRepo.findByUsernameCaseSensitive(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
		return new MyUserDetails(employee);
	}
}