package com.redbooks.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.redbooks.admin.entity.AuditLog;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
	List<AuditLog> findByAdminId(Long adminId);
}