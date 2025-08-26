package com.redbooks.admin.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	public JwtFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String header = request.getHeader("Authorization");

		if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
			String token = header.substring(7);
			try {
				Jws<Claims> claims = jwtUtil.validateToken(token);
				String subject = claims.getBody().getSubject();
				String role = (String) claims.getBody().get("role");
				if (!role.startsWith("ROLE_")) {
					role = "ROLE_" + role; // ensure Spring Security compatible role
				}
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(subject, null,
						List.of(new SimpleGrantedAuthority(role)));
				SecurityContextHolder.getContext().setAuthentication(auth);
			} catch (JwtException | IllegalArgumentException ex) {
				// Token invalid
				SecurityContextHolder.clearContext();
				// optional: log ex.getMessage()
			}
		}

		filterChain.doFilter(request, response);
	}
}
