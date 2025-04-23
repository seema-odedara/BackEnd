package com.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.employee.domain.Employee;
import com.employee.proxy.EmployeeProxy;
import com.employee.proxy.LoginRequest;
import com.employee.proxy.LoginResponse;
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
	public ResponseEntity<String> saveEmployee(@RequestPart("employee") EmployeeProxy employeeProxy, @RequestPart("profileImage") MultipartFile profileImage) {
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
	public ResponseEntity<String> updateEmployeeById(@PathVariable Long id, @RequestPart("employee") EmployeeProxy employeeProxy, @RequestPart("profileImage") MultipartFile profileImage) {
		return new ResponseEntity<String>(employeeService.updateEmployeeById(id, employeeProxy, profileImage), HttpStatus.OK);
	}

	@DeleteMapping("/deleteEmployeeById/{id}")
	public ResponseEntity<String> deleteEmployeeById(@PathVariable Long id) {
		return new ResponseEntity<String>(employeeService.deleteEmployeeById(id), HttpStatus.OK);
	}
	
	@GetMapping("getEmployeesByPage/{pageNumber}/{perPageCount}/{sortColumn}/{sortDirection}")
	public Page<Employee> getEmployeesByPage(@PathVariable Integer pageNumber,@PathVariable Integer perPageCount,@PathVariable String sortColumn,@PathVariable String sortDirection) {
		return employeeService.getEmployeesByPage(pageNumber, perPageCount, sortColumn , sortDirection);
	}
	
	@PostMapping("/save-bulk-users/{numberOfUsers}")
	public String saveBulkUsers(@PathVariable Integer numberOfUsers) {
		return employeeService.saveBulkUsers(numberOfUsers);
	}
	
//	private final Cage cage = new GCage();
//
//    @GetMapping(value = "/getCaptcha", produces = MediaType.IMAGE_JPEG_VALUE)
//    public ResponseEntity<byte[]> getCaptcha(HttpSession session) throws IOException {
//        String token = cage.getTokenGenerator().next();
//        session.setAttribute("captcha", token);
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        cage.draw(token, os);
//        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(os.toByteArray());
//    }
//
//    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
//    @PostMapping("/verifyCaptcha")
//    public ResponseEntity<String> verifyCaptcha(@RequestBody Map<String, String> payload, HttpSession session) {
//    	System.err.println("in controller");
//        String userInput = payload.get("captcha");
//        String sessionCaptcha = (String) session.getAttribute("captcha");
//
//        if (userInput != null && userInput.equals(sessionCaptcha)) {
//            return ResponseEntity.ok("Captcha Verified");
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Captcha Invalid");
//        }
//    }
	
	@GetMapping("/getAdminByUsername/{username}")
	public ResponseEntity<EmployeeProxy> getAdminByUsername(@PathVariable String username) {
		return new ResponseEntity<EmployeeProxy>(employeeService.getAdminByUsername(username), HttpStatus.OK);
	}

}
