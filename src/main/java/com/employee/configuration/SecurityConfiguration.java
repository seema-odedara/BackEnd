package com.employee.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.employee.exception.JwtExceptionHandler;
import com.employee.filter.JwtFilter;
import com.employee.service.impl.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Autowired
	private JwtFilter jwtFilter;
	
	@Autowired
	JwtExceptionHandler jwtExceptionHandler;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(c -> c.disable());
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/employee/login", "/employee/saveEmployee/**", "/employee/getCaptcha/**", "/employee/verifyCaptcha/**","/employee/validate-token/**" , "/employee/forgot-password/**", "/employee/reset-password/**").permitAll()
				.anyRequest().authenticated());
		http.exceptionHandling(ex->ex.authenticationEntryPoint(jwtExceptionHandler));
		http.exceptionHandling(ex->ex.accessDeniedHandler(jwtExceptionHandler));
		http.formLogin(form -> form.disable()); 
		http.httpBasic(httpBasic -> httpBasic.disable());
		http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return myUserDetailsService;
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuth = new DaoAuthenticationProvider();
		daoAuth.setPasswordEncoder(bCryptPasswordEncoder());
		daoAuth.setUserDetailsService(userDetailsService());
		return daoAuth;
	}
	
	@Bean 
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
