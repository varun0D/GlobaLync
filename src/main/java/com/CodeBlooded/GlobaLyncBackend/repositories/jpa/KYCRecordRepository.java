package com.CodeBlooded.GlobaLyncBackend.repositories.jpa;

import com.CodeBlooded.GlobaLyncBackend.entities.jpa.KYCRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KYCRecordRepository extends JpaRepository<KYCRecord, String> {
}
