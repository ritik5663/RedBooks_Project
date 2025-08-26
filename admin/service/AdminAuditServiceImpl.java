package com.redbooks.admin.service;

import com.redbooks.admin.entity.AuditLog;
import com.redbooks.admin.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuditServiceImpl implements AdminAuditService {

	private final AuditLogRepository repo;

	@Override
	public void record(Long adminId, String action, String targetType, Long targetId, String details) {
		if (adminId == null) {
			System.out.println("Warning: Audit record not created. Admin ID is null.");
			return;
		}

		AuditLog log = AuditLog.builder().adminId(adminId).action(action).targetType(targetType).targetId(targetId)
				.details(details).build();

		repo.save(log);
	}
}
