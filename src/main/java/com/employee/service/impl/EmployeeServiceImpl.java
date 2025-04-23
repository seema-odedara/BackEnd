package com.employee.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.employee.domain.Employee;
import com.employee.enums.GenderEnum;
import com.employee.enums.RoleEnum;
import com.employee.proxy.EmployeeProxy;
import com.employee.proxy.LoginRequest;
import com.employee.proxy.LoginResponse;
import com.employee.repository.EmployeeRepo;
import com.employee.service.EmployeeService;
import com.employee.utils.JwtUtils;
import com.employee.utils.MapperUtils;
import com.github.javafaker.Faker;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private MapperUtils mapper;

	@Autowired
	private AuthenticationManager authManeger;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public LoginResponse login(LoginRequest loginRequest) {
		Authentication unVerifiedAuth = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
				loginRequest.getPassword());

		Authentication verifiedAuth = authManeger.authenticate(unVerifiedAuth);
		if (verifiedAuth.isAuthenticated()) {
			return new LoginResponse(loginRequest.getUsername(), jwtUtils.geneateToken(loginRequest.getUsername()),
					(List<SimpleGrantedAuthority>) verifiedAuth.getAuthorities());
		}
		return new LoginResponse(loginRequest.getUsername(), "failed Token", null);
	}

	@Override
	public String saveEmployee(EmployeeProxy employeeProxy, MultipartFile profileImage) {

		String fileName = null;
		String path = null;

		try {

			String inPath = new ClassPathResource("").getFile().getAbsolutePath();
			path = inPath + File.separator + "static" + File.separator + "documents";
			File f = new File(path);
			if (!f.exists()) {
				f.mkdirs();
			}

			fileName = profileImage.getOriginalFilename();
			String absolutePath = path + File.separator + fileName;

			Files.copy(profileImage.getInputStream(), Paths.get(absolutePath), StandardCopyOption.REPLACE_EXISTING);

			employeeProxy.setFileName(fileName);
			employeeProxy.setFileData(profileImage.getBytes());
			employeeProxy.setFileSize((profileImage.getSize() / 1000) + "kb");
			employeeProxy.setContentType(profileImage.getContentType());

		} catch (IOException e) {
			e.printStackTrace();
		}

		employeeProxy.setPassword(bCryptPasswordEncoder.encode(employeeProxy.getPassword()));
		employeeRepo.save(mapper.employeeProxyToEntity(employeeProxy));
		return "Employee saved successfully...";
	}

	@Override
	public List<EmployeeProxy> getAllEmployees() {
		return mapper.convertEmployeeListEntityToProxy(employeeRepo.findAll());
	}

	@Override
	public EmployeeProxy getEmployeeById(Long id) {
		Employee employee = employeeRepo.findById(id).get();
		return mapper.employeeEntityToProxy(employee);
	}

	@Override
	public String updateEmployeeById(Long id, EmployeeProxy employeeProxy, MultipartFile profileImage) {
		Optional<Employee> employee = employeeRepo.findById(id);
		if (employee.isPresent()) {

			Employee emp = employee.get();
			Predicate<String> p = s -> Objects.isNull(s) || s.equals("");
//			Predicate<Date> dateCheck = d -> d == null;

			emp.setName(p.test(employeeProxy.getName()) ? emp.getName() : employeeProxy.getName());

			emp.setDob(p.test(employeeProxy.getDob()) ? emp.getDob() : employeeProxy.getDob());

			emp.setEmail(p.test(employeeProxy.getEmail()) ? emp.getEmail() : employeeProxy.getEmail());

			emp.setUsername(p.test(employeeProxy.getUsername()) ? emp.getUsername() : employeeProxy.getUsername());

			emp.setPassword(p.test(employeeProxy.getPassword()) ? emp.getPassword()
					: bCryptPasswordEncoder.encode(employeeProxy.getPassword()));

			if (employeeProxy.getGender() != null) {
				emp.setGender(employeeProxy.getGender());
			}

			emp.setAddress(p.test(employeeProxy.getAddress()) ? emp.getAddress() : employeeProxy.getAddress());

			emp.setContactNumber(p.test(employeeProxy.getContactNumber()) ? emp.getContactNumber()
					: employeeProxy.getContactNumber());
			
			if (employeeProxy.getPinCode() != null) {
				emp.setPinCode(employeeProxy.getPinCode());
			}

			if (employeeProxy.getAccessRole() != null) {
				emp.setAccessRole(employeeProxy.getAccessRole());
			}

			if (employeeProxy.getIsActive() != null) {
				emp.setIsActive(employeeProxy.getIsActive());
			}

			if (profileImage != null && !profileImage.isEmpty()) {
				try {
					String inPath = new ClassPathResource("").getFile().getAbsolutePath();
					String path = inPath + File.separator + "static" + File.separator + "documents";

					File dir = new File(path);
					if (!dir.exists()) {
						dir.mkdirs();
					}

					String fileName = profileImage.getOriginalFilename();
					String absolutePath = path + File.separator + fileName;

					Files.copy(profileImage.getInputStream(), Paths.get(absolutePath),
							StandardCopyOption.REPLACE_EXISTING);

					emp.setFileName(fileName);
					emp.setFileData(profileImage.getBytes());
					emp.setFileSize((profileImage.getSize() / 1000) + "kb");
					emp.setContentType(profileImage.getContentType());

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			employeeRepo.save(emp);

			return "Employee updated successfully...";
		}
		return "Employee not found with given id";
	}

	@Override
	public String deleteEmployeeById(Long id) {
		employeeRepo.deleteById(id);
		return "Employee deleted successfully...";
	}

//	@Override
//	public Page<Employee> getEmployeesByPage(Integer pageNumber, Integer perPageCount, String sortColumn,
//			String sortDirection) {
//		return employeeRepo.findAll(PageRequest.of(pageNumber - 1, perPageCount,
//				sortDirection.equals("asc") ? Direction.ASC : Direction.DESC, sortColumn));
//	}
	
	@Override
	public Page<Employee> getEmployeesByPage(Integer pageNumber, Integer perPageCount, String sortColumn,
	                                         String sortDirection) {

	    Sort sort = Sort.by(sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortColumn);
	    Pageable pageable = PageRequest.of(pageNumber - 1, perPageCount, sort);

	    // Use RoleEnum.USER instead of "USER"
	    return employeeRepo.findByAccessRole(RoleEnum.USER, pageable);
	}

	
	@Override
	public String saveBulkUsers(Integer numberOfUsers) {
		for (int i = 1; i <= numberOfUsers; i++) {
			employeeRepo.save(generateUsers());
		}
		return "Bulk students saved successfully...";
	}

	private Employee generateUsers() {
		Faker f = new Faker();
		Employee emp = new Employee();
		
		String fname = f.name().firstName();
		emp.setName(fname);
		
		emp.setDob((f.date().birthday()).toString());
		
		emp.setEmail(fname + "@gmail.com");
		
		emp.setUsername(fname);
		
		emp.setPassword(f.internet().password());
		
		emp.setGender(GenderEnum.values()[new Random().nextInt(GenderEnum.values().length)]);
		
		emp.setAddress(f.address().fullAddress());
		
		emp.setContactNumber(f.phoneNumber().phoneNumber());
		
		try {
			emp.setPinCode(Integer.parseInt(f.address().zipCode()));
		} catch (Exception e) {
			emp.setPinCode(f.number().numberBetween(100000, 999999));
		}
		
		emp.setAccessRole(RoleEnum.valueOf("USER"));
		
		emp.setIsActive(true);
		
		 String IMAGE_DIR = "src/main/resources/static/images";
		 Random r = new Random();
		
		 try {
	            File[] imageFiles = new File(IMAGE_DIR).listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png"));
	            if (imageFiles != null && imageFiles.length > 0) {
	                File imageFile = imageFiles[r.nextInt(imageFiles.length)];
	                byte[] fileData = Files.readAllBytes(imageFile.toPath());

	                emp.setFileName(imageFile.getName());
	                emp.setFileData(fileData);
	                emp.setFileSize((fileData.length / 1000) + "kb");
	                emp.setContentType(Files.probeContentType(imageFile.toPath()));
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        System.out.println("Fake employee created: " + emp.getUsername());
	    
		
		return emp;
	}

	@Override
	public EmployeeProxy getAdminByUsername(String username) {
		return mapper.employeeEntityToProxy(employeeRepo.findByUsernameCaseSensitive(username).get());
	}

}
