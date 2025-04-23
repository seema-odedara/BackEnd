package com.employee.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.employee.service.impl.MyUserDetailsService;
import com.employee.utils.JwtUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
	@Autowired
	JwtUtils jwtU;

	@Autowired
	MyUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String tokenFromRequest = jwtU.getTokenFromRequest(request);
			if (tokenFromRequest != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				String userName = jwtU.extractUserName(tokenFromRequest);

				UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

				if (userDetails != null && jwtU.validateToken(tokenFromRequest, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);

				}
			}
			filterChain.doFilter(request, response);
			
		} catch (ExpiredJwtException | SignatureException e) {
			request.setAttribute("jwt_exception", e);
			throw new RuntimeException(e);
		}

	}
}