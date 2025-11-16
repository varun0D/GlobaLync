package com.CodeBlooded.GlobaLyncBackend.repositories.jpa;


import com.CodeBlooded.GlobaLyncBackend.entities.jpa.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {
    // add custom queries if needed later
}
