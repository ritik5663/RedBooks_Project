package com.redbooks.admin.service;

import com.redbooks.admin.dto.*;
import com.redbooks.admin.entity.AuditLog;
import com.redbooks.admin.entity.Admin;
import com.redbooks.publisher.entity.Publisher;
import com.redbooks.school.entity.School;
import com.redbooks.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface AdminService {
	// auth/profile
	String login(String email, String password);

	AdminDto getProfile(String email);

	AdminDto updateProfile(String email, AdminDto dto);

	void changePassword(String email, String oldPassword, String newPassword);

	// schools
	List<School> getPendingSchools();

	void approveSchool(String adminEmail, Long schoolId, String remarks);

	void rejectSchool(String adminEmail, Long schoolId, String remarks);

	List<School> listSchools(Optional<String> status);

	School getSchool(Long schoolId);

	void suspendSchool(String adminEmail, Long schoolId, String remarks);

	void activateSchool(String adminEmail, Long schoolId, String remarks);

	void deleteSchool(String adminEmail, Long schoolId, String remarks);

	// publishers
	List<Publisher> getPendingPublishers();

	void approvePublisher(String adminEmail, Long publisherId, String remarks);

	void rejectPublisher(String adminEmail, Long publisherId, String remarks);

	List<Publisher> listPublishers();

	Publisher getPublisher(Long publisherId);

	// users
//    List<User> listUsers(Optional<Long> schoolId);
	User getUser(Long userId);

	void suspendUser(String adminEmail, Long userId, String remarks);

	void activateUser(String adminEmail, Long userId, String remarks);

	void deleteUser(String adminEmail, Long userId, String remarks);

	// dashboard & audit
	com.redbooks.admin.dto.DashboardDto dashboard();

	List<AuditLog> listAuditLogs();
}