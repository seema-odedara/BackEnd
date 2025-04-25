package com.employee.controller;

import java.io.Console;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.employee.domain.Employee;
import com.employee.proxy.EmployeeProxy;
import com.employee.proxy.LoginRequest;
import com.employee.proxy.LoginResponse;
import com.employee.proxy.ResetPasswordRequest;
import com.employee.service.EmployeeService;

@RestController
@RequestMapping("/employee")
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
		return new ResponseEntity<LoginResponse>(employeeService.login(loginRequest), HttpStatus.OK);
	}

	@PostMapping("/saveEmployee")
	public ResponseEntity<String> saveEmployee(@RequestPart("employee") EmployeeProxy employeeProxy,
			@RequestPart("profileImage") MultipartFile profileImage) {
		System.err.println("controller");
		return new ResponseEntity<String>(employeeService.saveEmployee(employeeProxy, profileImage), HttpStatus.OK);

	}

	@GetMapping("/getAllEmployees")
	public ResponseEntity<List<EmployeeProxy>> getAllEmployees() {
		return new ResponseEntity<List<EmployeeProxy>>(employeeService.getAllEmployees(), HttpStatus.OK);
	}

	@GetMapping("/getEmployeeById/{id}")
	public ResponseEntity<EmployeeProxy> getEmployeeById(@PathVariable Long id) {
		return new ResponseEntity<EmployeeProxy>(employeeService.getEmployeeById(id), HttpStatus.OK);
	}

	@PutMapping("/updateEmployeeById/{id}")
	public ResponseEntity<String> updateEmployeeById(@PathVariable Long id,
			@RequestPart("employee") EmployeeProxy employeeProxy,
			@RequestPart("profileImage") MultipartFile profileImage) {
		return new ResponseEntity<String>(employeeService.updateEmployeeById(id, employeeProxy, profileImage),
				HttpStatus.OK);
	}

	@DeleteMapping("/deleteEmployeeById/{id}")
	public ResponseEntity<String> deleteEmployeeById(@PathVariable Long id) {
		return new ResponseEntity<String>(employeeService.deleteEmployeeById(id), HttpStatus.OK);
	}

	@GetMapping("getEmployeesByPage/{pageNumber}/{perPageCount}/{sortColumn}/{sortDirection}")
	public Page<Employee> getEmployeesByPage(@PathVariable Integer pageNumber, @PathVariable Integer perPageCount,
			@PathVariable String sortColumn, @PathVariable String sortDirection) {
		return employeeService.getEmployeesByPage(pageNumber, perPageCount, sortColumn, sortDirection);
	}

	@PostMapping("/save-bulk-users/{numberOfUsers}")
	public String saveBulkUsers(@PathVariable Integer numberOfUsers) {
		return employeeService.saveBulkUsers(numberOfUsers);
	}

	@GetMapping("/getAdminByUsername/{username}")
	public ResponseEntity<EmployeeProxy> getAdminByUsername(@PathVariable String username) {
		return new ResponseEntity<EmployeeProxy>(employeeService.getAdminByUsername(username), HttpStatus.OK);
	}

	@PostMapping("/forgot-password/{payload}")
	public ResponseEntity<?> forgotPassword(@PathVariable String payload) {
		System.err.println("controller");
		System.err.println(payload);
		employeeService.generateResetToken(payload);
		Map<String, String> response = new HashMap<>();
		response.put("message", "Reset link sent");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/validate-token/{token}")
	public ResponseEntity<?> validateToken(@PathVariable String token) {
		boolean valid = employeeService.validateToken(token);
		return ResponseEntity.ok(valid);
	}

	@PostMapping("/reset-password/{token}")
	public ResponseEntity<?> resetPassword(@PathVariable String token, @RequestBody ResetPasswordRequest password) {
		System.err.println(password.getPassword());
		employeeService.resetPassword(token, password.getPassword());
		Map<String, String> response = new HashMap<>();
		response.put("message", "Password reset successful");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/search/{name}/{pageNumber}/{perPageCount}")
	public ResponseEntity<Page<Employee>> search(@PathVariable String name, @PathVariable Integer pageNumber, @PathVariable Integer perPageCount) {
		Page<Employee> emp = employeeService.search(name, pageNumber, perPageCount);
		return ResponseEntity.ok(emp);
	}
	
	@GetMapping("/DownloadExcelFromDatabase")
	public ResponseEntity<?> DownloadExcelFromDatabase() {
		final String FileName = "employee_data.xlsx";
		byte[] downloadExcelFromDatabase = employeeService.DownloadExcelFromDatabase();
		return ResponseEntity
				.status(HttpStatus.OK)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + FileName + "\"")
				.body(downloadExcelFromDatabase);
	}

}
