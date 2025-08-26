package com.redbooks.admin.service;

import com.redbooks.admin.dto.*;
import com.redbooks.admin.entity.*;
import com.redbooks.admin.mapper.AdminMapper;
import com.redbooks.admin.repository.AdminRepository;
import com.redbooks.admin.repository.AuditLogRepository;
import com.redbooks.admin.security.JwtUtil;
import com.redbooks.publisher.entity.Publisher;
import com.redbooks.publisher.repository.PublisherRepository;
import com.redbooks.school.entity.School;
import com.redbooks.school.repository.SchoolRepository;
import com.redbooks.user.entity.User;
import com.redbooks.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.modelmapper.ModelMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

	private final AdminRepository adminRepository;
	private final AuditLogRepository auditLogRepository;
	private final SchoolRepository schoolRepository;
	private final PublisherRepository publisherRepository;
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;
	private final PasswordEncoder passwordEncoder; // Injected as Bean
	private final AdminMapper adminMapper;

	@Override
	public String login(String email, String password) {
		Admin admin = adminRepository.findByEmail(email)
				.orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
		if (!passwordEncoder.matches(password, admin.getPassword()))
			throw new BadCredentialsException("Invalid credentials");

		return jwtUtil.generateToken(admin.getEmail(), admin.getRole());
	}

	@Override
	public AdminDto getProfile(String email) {
		Admin admin = adminRepository.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("Admin not found"));
		return adminMapper.toDto(admin);
	}

	@Override
	public AdminDto updateProfile(String email, AdminDto dto) {
		Admin admin = adminRepository.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("Admin not found"));
		admin.setName(dto.getName());
		admin.setEmail(dto.getEmail());
		admin.setUpdatedAt(LocalDateTime.now());
		adminRepository.save(admin);
		return adminMapper.toDto(admin);
	}

	@Override
	public void changePassword(String email, String oldPassword, String newPassword) {
		Admin admin = adminRepository.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("Admin not found"));
		if (!passwordEncoder.matches(oldPassword, admin.getPassword()))
			throw new RuntimeException("Old password incorrect");

		admin.setPassword(passwordEncoder.encode(newPassword));
		adminRepository.save(admin);
	}

	// ---------------- SCHOOLS ----------------
	@Override
	public List<School> getPendingSchools() {
		return schoolRepository.findAll().stream().filter(s -> s.getSchoolStatus() == SchoolStatus.PENDING)
				.collect(Collectors.toList());
	}

	@Override
	public void approveSchool(String adminEmail, Long schoolId, String remarks) {
		School school = getSchoolById(schoolId);
		school.setSchoolStatus(SchoolStatus.APPROVED);
		schoolRepository.save(school);

		saveAudit(adminEmail, "APPROVE_SCHOOL", "SCHOOL", schoolId, remarks);
	}

	@Override
	public void rejectSchool(String adminEmail, Long schoolId, String remarks) {
		School school = getSchoolById(schoolId);
		school.setSchoolStatus(SchoolStatus.REJECTED);
		schoolRepository.save(school);

		saveAudit(adminEmail, "REJECT_SCHOOL", "SCHOOL", schoolId, remarks);
	}

	@Override
	public List<School> listSchools(Optional<String> status) {
		if (status.isPresent()) {
			SchoolStatus st;
			try {
				st = SchoolStatus.valueOf(status.get().toUpperCase());
			} catch (IllegalArgumentException ex) {
				throw new IllegalArgumentException("Invalid school status: " + status.get());
			}
			return schoolRepository.findAll().stream().filter(s -> s.getSchoolStatus() == st)
					.collect(Collectors.toList());
		}
		return schoolRepository.findAll();
	}

	@Override
	public School getSchool(Long schoolId) {
		return getSchoolById(schoolId);
	}

	@Override
	public void suspendSchool(String adminEmail, Long schoolId, String remarks) {
		School school = getSchoolById(schoolId);
		school.setSchoolStatus(SchoolStatus.SUSPENDED);
		schoolRepository.save(school);

		saveAudit(adminEmail, "SUSPEND_SCHOOL", "SCHOOL", schoolId, remarks);
	}

	@Override
	public void activateSchool(String adminEmail, Long schoolId, String remarks) {
		School school = getSchoolById(schoolId);
		school.setSchoolStatus(SchoolStatus.ACTIVE);
		schoolRepository.save(school);

		saveAudit(adminEmail, "ACTIVATE_SCHOOL", "SCHOOL", schoolId, remarks);
	}

	@Override
	public void deleteSchool(String adminEmail, Long schoolId, String remarks) {
		schoolRepository.deleteById(schoolId);
		saveAudit(adminEmail, "DELETE_SCHOOL", "SCHOOL", schoolId, remarks);
	}

	@Override
	public List<Publisher> getPendingPublishers() {
		return publisherRepository.findAll().stream().filter(p -> p.getStatus() == PublisherStatus.PENDING)
				.collect(Collectors.toList());
	}

	@Override
	public void approvePublisher(String adminEmail, Long publisherId, String remarks) {
		Publisher publisher = getPublisherById(publisherId);
		publisher.setStatus(PublisherStatus.APPROVED);
		publisherRepository.save(publisher);

		saveAudit(adminEmail, "APPROVE_PUBLISHER", "PUBLISHER", publisherId, remarks);
	}

	@Override
	public void rejectPublisher(String adminEmail, Long publisherId, String remarks) {
		Publisher publisher = getPublisherById(publisherId);
		publisher.setStatus(PublisherStatus.REJECTED);
		publisherRepository.save(publisher);

		saveAudit(adminEmail, "REJECT_PUBLISHER", "PUBLISHER", publisherId, remarks);
	}

	@Override
	public List<Publisher> listPublishers() {
		return publisherRepository.findAll();
	}

	@Override
	public Publisher getPublisher(Long publisherId) {
		return getPublisherById(publisherId);
	}

	@Override
	public User getUser(Long userId) {
		return getUserById(userId);
	}

	@Override
	public void suspendUser(String adminEmail, Long userId, String remarks) {
		User user = getUserById(userId);
		user.setStatus(UserStatus.SUSPENDED);
		userRepository.save(user);

		saveAudit(adminEmail, "SUSPEND_USER", "USER", userId, remarks);
	}

	@Override
	public void activateUser(String adminEmail, Long userId, String remarks) {
		User user = getUserById(userId);
		user.setStatus(UserStatus.ACTIVE);
		userRepository.save(user);

		saveAudit(adminEmail, "ACTIVATE_USER", "USER", userId, remarks);
	}

	@Override
	public void deleteUser(String adminEmail, Long userId, String remarks) {
		userRepository.deleteById(userId);
		saveAudit(adminEmail, "DELETE_USER", "USER", userId, remarks);
	}

	@Override
	public DashboardDto dashboard() {
		long totalSchools = schoolRepository.count();
		long pendingSchools = schoolRepository.findAll().stream()
				.filter(s -> s.getSchoolStatus() == SchoolStatus.PENDING).count();

		long totalPublishers = publisherRepository.count();
		long pendingPublishers = publisherRepository.findAll().stream()
				.filter(p -> p.getStatus() == PublisherStatus.PENDING).count();

		long totalUsers = userRepository.count();

		return DashboardDto.builder().totalSchools(totalSchools).pendingSchools(pendingSchools)
				.totalPublishers(totalPublishers).pendingPublishers(pendingPublishers).totalUsers(totalUsers)
				.pendingRequests(0L) // ✅ int → long fix
				.build();
	}

	@Override
	public List<AuditLog> listAuditLogs() {
		return auditLogRepository.findAll();
	}

	private void saveAudit(String adminEmail, String action, String targetType, Long targetId, String details) {
		auditLogRepository.save(AuditLog.builder().adminId(getAdminId(adminEmail)).action(action).targetType(targetType)
				.targetId(targetId).details(details).timestamp(LocalDateTime.now()).build());
	}

	private Long getAdminId(String email) {
		return adminRepository.findByEmail(email).map(Admin::getId)
				.orElseThrow(() -> new RuntimeException("Admin not found for audit"));
	}

	private School getSchoolById(Long id) {
		return schoolRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("School not found"));
	}

	private Publisher getPublisherById(Long id) {
		return publisherRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Publisher not found"));
	}

	private User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
	}
}
