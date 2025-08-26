package com.redbooks.admin.service;

import com.redbooks.admin.entity.Admin;
import com.redbooks.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSetupRunner implements ApplicationRunner {

	private final AdminRepository adminRepo;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(ApplicationArguments args) {
		String defaultEmail = "ritik@redbooks.com";
		if (adminRepo.findByEmail(defaultEmail).isEmpty()) {
			Admin admin = Admin.builder().name("Super Admin").email(defaultEmail)
					.password(passwordEncoder.encode("Ritik@123")).role("ADMIN").status("ACTIVE").build();
			adminRepo.save(admin);
			System.out.println("Default admin created: " + defaultEmail);
		}
	}
}
