package com.redbooks.admin.contoller;

import com.redbooks.admin.dto.*;
import com.redbooks.admin.entity.AuditLog;
import com.redbooks.admin.service.AdminService;
import com.redbooks.publisher.entity.Publisher;
import com.redbooks.school.entity.School;
import com.redbooks.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
		String token = adminService.login(body.get("email"), body.get("password"));
		return ResponseEntity.ok(Map.of("token", token));
	}

	@GetMapping("/profile")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public AdminDto profile(Authentication auth) {
		return adminService.getProfile(auth.getName());
	}

	@PutMapping("/profile")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public AdminDto updateProfile(Authentication auth, @RequestBody AdminDto dto) {
		return adminService.updateProfile(auth.getName(), dto);
	}

	@PostMapping("/change-password")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> changePassword(Authentication auth, @RequestParam String oldPassword,
			@RequestParam String newPassword) {
		adminService.changePassword(auth.getName(), oldPassword, newPassword);
		return ResponseEntity.ok(Map.of("msg", "password changed"));
	}

	@GetMapping("/schools/pending")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<School> pendingSchools() {
		return adminService.getPendingSchools();
	}

	@PostMapping("/schools/{schoolId}/approve")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> approveSchool(Authentication auth, @PathVariable Long schoolId,
			@RequestBody(required = false) ActionRequest body) {
		adminService.approveSchool(auth.getName(), schoolId, body != null ? body.getRemarks() : null);
		return ResponseEntity.ok(Map.of("msg", "approved"));
	}

	@PostMapping("/schools/{schoolId}/reject")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> rejectSchool(Authentication auth, @PathVariable Long schoolId,
			@RequestBody ActionRequest body) {
		adminService.rejectSchool(auth.getName(), schoolId, body.getRemarks());
		return ResponseEntity.ok(Map.of("msg", "rejected"));
	}

	@GetMapping("/schools")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<School> listSchools(@RequestParam Optional<String> status) {
		return adminService.listSchools(status);
	}

	@GetMapping("/schools/{schoolId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<School> getSchool(@PathVariable Long schoolId) {
		return ResponseEntity.ok(adminService.getSchool(schoolId));
	}

	@PutMapping("/schools/{schoolId}/suspend")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> suspendSchool(Authentication auth, @PathVariable Long schoolId,
			@RequestBody(required = false) ActionRequest body) {
		adminService.suspendSchool(auth.getName(), schoolId, body != null ? body.getRemarks() : null);
		return ResponseEntity.ok(Map.of("msg", "suspended"));
	}

	@PutMapping("/schools/{schoolId}/activate")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> activateSchool(Authentication auth, @PathVariable Long schoolId,
			@RequestBody(required = false) ActionRequest body) {
		adminService.activateSchool(auth.getName(), schoolId, body != null ? body.getRemarks() : null);
		return ResponseEntity.ok(Map.of("msg", "activated"));
	}

	@DeleteMapping("/schools/{schoolId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> deleteSchool(Authentication auth, @PathVariable Long schoolId,
			@RequestBody(required = false) ActionRequest body) {
		adminService.deleteSchool(auth.getName(), schoolId, body != null ? body.getRemarks() : null);
		return ResponseEntity.ok(Map.of("msg", "deleted"));
	}

	@GetMapping("/publishers/pending")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Publisher> pendingPublishers() {
		return adminService.getPendingPublishers();
	}

	@PostMapping("/publishers/{publisherId}/approve")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> approvePublisher(Authentication auth, @PathVariable Long publisherId,
			@RequestBody(required = false) ActionRequest body) {
		adminService.approvePublisher(auth.getName(), publisherId, body != null ? body.getRemarks() : null);
		return ResponseEntity.ok(Map.of("msg", "approved"));
	}

	@PostMapping("/publishers/{publisherId}/reject")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> rejectPublisher(Authentication auth, @PathVariable Long publisherId,
			@RequestBody ActionRequest body) {
		adminService.rejectPublisher(auth.getName(), publisherId, body.getRemarks());
		return ResponseEntity.ok(Map.of("msg", "rejected"));
	}

	@GetMapping("/publishers")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Publisher> listPublishers() {
		return adminService.listPublishers();
	}

	@GetMapping("/publishers/{publisherId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Publisher> getPublisher(@PathVariable Long publisherId) {
		return ResponseEntity.ok(adminService.getPublisher(publisherId));
	}

	@GetMapping("/users/{userId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<User> getUser(@PathVariable Long userId) {
		return ResponseEntity.ok(adminService.getUser(userId));
	}

	@PutMapping("/users/{userId}/suspend")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> suspendUser(Authentication auth, @PathVariable Long userId,
			@RequestBody(required = false) ActionRequest body) {
		adminService.suspendUser(auth.getName(), userId, body != null ? body.getRemarks() : null);
		return ResponseEntity.ok(Map.of("msg", "suspended"));
	}

	@PutMapping("/users/{userId}/activate")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> activateUser(Authentication auth, @PathVariable Long userId,
			@RequestBody(required = false) ActionRequest body) {
		adminService.activateUser(auth.getName(), userId, body != null ? body.getRemarks() : null);
		return ResponseEntity.ok(Map.of("msg", "activated"));
	}

	@DeleteMapping("/users/{userId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> deleteUser(Authentication auth, @PathVariable Long userId,
			@RequestBody(required = false) ActionRequest body) {
		adminService.deleteUser(auth.getName(), userId, body != null ? body.getRemarks() : null);
		return ResponseEntity.ok(Map.of("msg", "deleted"));
	}

	@GetMapping("/dashboard")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public DashboardDto dashboard() {
		return adminService.dashboard();
	}

	@GetMapping("/audit-logs")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<AuditLog> auditLogs() {
		return adminService.listAuditLogs();
	}
}
