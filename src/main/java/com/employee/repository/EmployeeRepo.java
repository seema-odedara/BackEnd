package com.employee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.employee.domain.Employee;
import com.employee.enums.RoleEnum;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {
//	public Optional<Employee> findByUsername(String username);
	
	@Query(value = "SELECT * FROM employee WHERE BINARY username = :username", nativeQuery = true)
	Optional<Employee> findByUsernameCaseSensitive(@Param("username") String username);
	
	Page<Employee> findByAccessRole(RoleEnum accessRole, Pageable pageable);
	
	Optional<Employee> findByEmail(String email);
    Optional<Employee> findByResetToken(String resetToken);
    
    Page<Employee> findByUsernameAndAccessRole(String name, RoleEnum accessRole, Pageable pageable);
}
