package com.CodeBlooded.GlobaLyncBackend.services;

import com.CodeBlooded.GlobaLyncBackend.entities.jpa.Transfer;
import com.CodeBlooded.GlobaLyncBackend.enums.TransferState;
import com.CodeBlooded.GlobaLyncBackend.repositories.jpa.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class StateMachineService {

    @Autowired
    private AuditService auditService;

    @Autowired
    private TransferRepository transferRepository;

    // ------------------------------------------------------
    // STATE TRANSITION MATRIX (FINAL, MATCHES PSP LOGIC)
    // ------------------------------------------------------
    private static final Map<TransferState, List<TransferState>> validTransitions =
            Map.ofEntries(

                    // 1) User created transfer
                    Map.entry(TransferState.CREATED,
                            List.of(TransferState.PSP_PENDING, TransferState.FAILED)),

                    // 2) Waiting for PSP to accept payment
                    Map.entry(TransferState.PSP_PENDING,
                            List.of(TransferState.PSP_CONFIRMED, TransferState.FAILED)),

                    // 3) PSP confirmed → apply FX lock
                    Map.entry(TransferState.PSP_CONFIRMED,
                            List.of(TransferState.FX_LOCKED, TransferState.FAILED)),

                    // 4) FX rate applied → reserve liquidity
                    Map.entry(TransferState.FX_LOCKED,
                            List.of(TransferState.LIQUIDITY_RESERVED, TransferState.FAILED)),

                    // 5) Liquidity reserved → ready for payout
                    Map.entry(TransferState.LIQUIDITY_RESERVED,
                            List.of(TransferState.READY_FOR_PAYOUT, TransferState.FAILED)),

                    // 6) Admin/system triggers payout
                    Map.entry(TransferState.READY_FOR_PAYOUT,
                            List.of(TransferState.PAYOUT_PROCESSING, TransferState.FAILED)),

                    // 7) PSP processing payout → waiting for callback
                    Map.entry(TransferState.PAYOUT_PROCESSING,
                            List.of(TransferState.PAYOUT_CONFIRMED, TransferState.FAILED)),

                    // 8) FINAL: payout done → COMPLETED
                    Map.entry(TransferState.PAYOUT_CONFIRMED,
                            List.of(TransferState.COMPLETED, TransferState.FAILED)),

                    // 9) COMPLETED → optional blockchain anchor
                    Map.entry(TransferState.COMPLETED,
                            List.of(TransferState.ANCHORED)),

                    Map.entry(TransferState.ANCHORED, List.of()),
                    Map.entry(TransferState.FAILED, List.of())
            );

    // ------------------------------------------------------
    // MAIN TRANSITION METHOD
    // ------------------------------------------------------
    @Transactional
    public Transfer transitionState(Transfer transfer, TransferState newState, String actor) {

        TransferState current = transfer.getState();

        List<TransferState> allowed = validTransitions.getOrDefault(current, List.of());

        if (!allowed.contains(newState)) {
            throw new IllegalStateException(
                    "❌ Invalid transition: " + current + " → " + newState
            );
        }

        transfer.setState(newState);
        transfer.setUpdatedAt(LocalDateTime.now());
        transferRepository.save(transfer);

        auditService.recordAudit(transfer.getId(), current, newState, actor);

        return transfer;
    }
}
