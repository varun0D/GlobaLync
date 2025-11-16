package com.CodeBlooded.GlobaLyncBackend.repositories.jpa;

import com.CodeBlooded.GlobaLyncBackend.entities.jpa.PoolLedger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LedgerRepository extends JpaRepository<PoolLedger, Long> {
    Optional<PoolLedger> findByPoolType(String poolType);
}
