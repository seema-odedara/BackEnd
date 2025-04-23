package com.employee.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.employee.domain.Employee;
import com.employee.proxy.EmployeeProxy;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MapperUtils {
	@Autowired
	private ObjectMapper mapper;
	
	public Employee employeeProxyToEntity(EmployeeProxy empProxy) {
		return mapper.convertValue(empProxy, Employee.class);
	}
	
	public EmployeeProxy employeeEntityToProxy(Employee employee) {
		return mapper.convertValue(employee, EmployeeProxy.class);
	}
	
	public List<EmployeeProxy> convertEmployeeListEntityToProxy(List<Employee> employeeList){
		return employeeList.stream().map(emp -> mapper.convertValue(emp, EmployeeProxy.class)).collect(Collectors.toList());
	}
}
