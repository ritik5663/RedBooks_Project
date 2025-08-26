package com.redbooks.admin.service;

public interface AdminAuditService {
	void record(Long adminId, String action, String targetType, Long targetId, String details);
}
