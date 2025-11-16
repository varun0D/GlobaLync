package com.CodeBlooded.GlobaLyncBackend.repositories.jpa;

import com.CodeBlooded.GlobaLyncBackend.entities.jpa.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByTransferId(String transferId);
}
