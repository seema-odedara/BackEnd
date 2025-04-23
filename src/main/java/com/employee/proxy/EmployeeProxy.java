package com.employee.proxy;

import com.employee.enums.GenderEnum;
import com.employee.enums.RoleEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeProxy {
	private Long id;

	private String name;

	private String dob;

	private String email;

	private String username;

	private String password;

	private GenderEnum gender;

	private String address;

	private String contactNumber;

	private Integer pinCode;

	private RoleEnum accessRole;

	private Boolean isActive;

	private String fileName;

	private byte[] fileData;

	private String fileSize;

	private String contentType;
}
