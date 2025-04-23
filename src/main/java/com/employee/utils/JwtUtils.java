package com.employee.utils;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {
	private static final String SECRETKEY = "azwq2OBEXq5BTdcM/mA7bvJoDeYH+g81iC7H4IVPShk=";

	public String generateSecretKey() {
		try {
			KeyGenerator algoKey = KeyGenerator.getInstance("HmacSHA256");
			SecretKey key = algoKey.generateKey();
			return Base64.getEncoder().encodeToString(key.getEncoded());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String geneateToken(String username) {
		Map<String, Object> claims = new HashMap<>();

		return Jwts.builder().claims().add(claims).subject(username).issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + (5 * 60 * 1000))).and().signWith(getSignKey()).compact();
	}
	
	 private SecretKey getSignKey() {
	        byte[] keyBytes = Decoders.BASE64.decode(SECRETKEY);
	        return Keys.hmacShaKeyFor(keyBytes);
	    }

	public String extractUserName(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
 
	private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token).getPayload();
	}

	public Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String userName = extractUserName(token);
		return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	public String getTokenFromRequest(HttpServletRequest request) {
		 String token = request.getHeader("Authorization");
		 if(token != null) {
			 return token.substring(7);
		 }
		 return null;
	}
}
