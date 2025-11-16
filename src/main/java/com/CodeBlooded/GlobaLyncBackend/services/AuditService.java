package com.CodeBlooded.GlobaLyncBackend.services;

import com.CodeBlooded.GlobaLyncBackend.entities.jpa.AuditLog;
import com.CodeBlooded.GlobaLyncBackend.enums.TransferState;
import com.CodeBlooded.GlobaLyncBackend.repositories.jpa.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.time.LocalDateTime;

@Service
public class AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    public void recordAudit(String transferId, TransferState prev, TransferState next, String actor) {
        AuditLog log = new AuditLog();
        log.setTransferId(transferId);
        log.setPreviousState(prev != null ? prev.toString() : null);
        log.setNewState(next.toString());
        log.setActor(actor);
        log.setTimestamp(LocalDateTime.now());
        log.setImmutableHash(hashAuditData(transferId, prev, next, actor));
        auditLogRepository.save(log);
    }

    private String hashAuditData(String transferId, TransferState prev, TransferState next, String actor) {
        try {
            String input = transferId + prev + next + actor + LocalDateTime.now();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
