package com.employee.domain;

import java.time.LocalDateTime;

import com.employee.enums.GenderEnum;
import com.employee.enums.RoleEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	private String name;

	private String dob;

	private String email;

	private String username;

	private String password;

	@Enumerated(EnumType.STRING)
	private GenderEnum gender;

	private String address;

	private String contactNumber;

	private Integer pinCode;

	@Enumerated(EnumType.STRING)
	private RoleEnum accessRole;

	private Boolean isActive;

	private String fileName;

	@Lob
	private byte[] fileData;

	private String fileSize;

	private String contentType;
	
	private String resetToken;
    private LocalDateTime tokenExpiry;

//	private String resetToken;

}
