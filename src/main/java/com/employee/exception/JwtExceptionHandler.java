package com.employee.exception;

import java.io.IOException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler  {
	@Autowired
	@Qualifier("handlerExceptionResolver")
	private HandlerExceptionResolver exceptionResolver;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
//		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//		response.setContentType("application/json");
//		response.getWriter().write("{\"error\": \"Unauthorized - Invalid or missing token\"}");
//		exceptionResolver.resolveException(request, response, null, authException);
		
		// Default values
        int status = HttpServletResponse.SC_UNAUTHORIZED;
        String error = "Unauthorized - Invalid or missing token";

        // Detect if JwtException was attached by filter
        Exception jwtException = (Exception) request.getAttribute("jwt_exception");
        if (jwtException != null) {
            if (jwtException instanceof ExpiredJwtException) {
                error = "Token has expired";
            } else if (jwtException instanceof SignatureException) {
                error = "Token signature is invalid";
            } else {
                error = jwtException.getMessage();
            }
        }

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", error);
        errorDetails.put("status", status);
        errorDetails.put("path", request.getRequestURI());

        response.setStatus(status);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), errorDetails);

	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		// Default values for authorization exception (Forbidden)
        int status = HttpServletResponse.SC_FORBIDDEN;
        String error = "Forbidden - You don’t have permission";

        // Send the response with error details for Access Denied
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", error);
        errorDetails.put("status", status);
        errorDetails.put("path", request.getRequestURI());

        response.setStatus(status);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), errorDetails);
	}
}