package com.CodeBlooded.GlobaLyncBackend.repositories.jpa;

import com.CodeBlooded.GlobaLyncBackend.entities.jpa.Transfer;
import com.CodeBlooded.GlobaLyncBackend.enums.TransferState;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TransferRepository extends JpaRepository<Transfer, String> {
    List<Transfer> findByState(TransferState state);
    Optional<Transfer> findByPspPaymentId(String pspPaymentId);
    Optional<Transfer> findByPayoutRef(String payoutRef);
}
