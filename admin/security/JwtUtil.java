package com.redbooks.admin.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

	private final String SECRET_KEY = "secret123dshiuhguihgkjdguiyhdifguyhiughiufgdiogiuhogihdugk";

	// Token generate karne ke liye
	public String generateToken(String username, String role) {
		return Jwts.builder().setSubject(username).claim("role", role).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 10)) // 10 hours
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	// Token validate karne ke liye
	public Jws<Claims> validateToken(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
	}

	// Username extract karne ke liye
	public String extractUsername(String token) {
		return validateToken(token).getBody().getSubject();
	}

	// Role extract karne ke liye
	public String extractRole(String token) {
		return (String) validateToken(token).getBody().get("role");
	}
}
