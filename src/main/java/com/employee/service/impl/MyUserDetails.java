package com.employee.service.impl;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.employee.domain.Employee;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MyUserDetails implements UserDetails{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Employee employee;

	public MyUserDetails(Employee employee) {
		super();
		this.employee = employee;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return  Collections.singleton(new SimpleGrantedAuthority(employee.getAccessRole().toString()));
	}

	@Override
	public String getPassword() {
		return employee.getPassword();
	}

	@Override
	public String getUsername() {
		return employee.getUsername();
	}
	
	@Override
	public boolean isEnabled() {
		return this.employee.getIsActive();
	}
}
