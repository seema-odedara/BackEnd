package com.employee.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.employee.domain.Employee;
import com.employee.proxy.EmployeeProxy;
import com.employee.proxy.LoginRequest;
import com.employee.proxy.LoginResponse;

public interface EmployeeService {
	public LoginResponse login(LoginRequest loginRequest);
	
	public String saveEmployee(EmployeeProxy employeeProxy, MultipartFile profileImage);
	
	public List<EmployeeProxy>getAllEmployees();
	
	public EmployeeProxy getEmployeeById(Long id);
	
	public String updateEmployeeById(Long id, EmployeeProxy employeeProxy, MultipartFile profileImage);
	
	public String deleteEmployeeById(Long id);
	
	public Page<Employee> getEmployeesByPage(Integer pageNumber, Integer perPageCount, String sortColumn, String sortDirection);
	
	public String saveBulkUsers(Integer numberOfUsers);
	
	public EmployeeProxy getAdminByUsername(String username);
}
