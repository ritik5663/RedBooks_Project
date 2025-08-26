package com.redbooks.admin.security;

import com.redbooks.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDetailsService implements UserDetailsService {
	private final AdminRepository repo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		var admin = repo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Admin not found with email: " + email));

		return new org.springframework.security.core.userdetails.User(admin.getEmail(), admin.getPassword(),
				List.of(new SimpleGrantedAuthority(admin.getRole())) // role already has "ROLE_" prefix
		);
	}
}
