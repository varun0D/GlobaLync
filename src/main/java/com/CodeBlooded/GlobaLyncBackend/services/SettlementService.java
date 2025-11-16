package com.CodeBlooded.GlobaLyncBackend.services;

import com.CodeBlooded.GlobaLyncBackend.entities.jpa.Settlement;
import com.CodeBlooded.GlobaLyncBackend.entities.jpa.Transfer;
import com.CodeBlooded.GlobaLyncBackend.enums.TransferState;
import com.CodeBlooded.GlobaLyncBackend.integration.blockchain.BlockchainAnchorService;
import com.CodeBlooded.GlobaLyncBackend.repositories.jpa.SettlementRepository;
import com.CodeBlooded.GlobaLyncBackend.repositories.jpa.TransferRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * SettlementService:
 * - aggregates completed transfers (COMPLETED) that are not yet anchored
 * - builds a settlement summary and creates a SHA-256 hash
 * - stores a Settlement record in MySQL
 * - calls BlockchainAnchorService.anchor(summaryHash)
 * - transitions included transfers to ANCHORED (via StateMachineService) and records audit
 */
@Service
public class SettlementService {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private SettlementRepository settlementRepository;

    @Autowired
    private BlockchainAnchorService blockchainAnchorService;

    @Autowired
    private StateMachineService stateMachineService;

    @Autowired
    private AuditService auditService;

    /**
     * Aggregate all transfers in COMPLETED state and not yet ANCHORED,
     * produce a summary, anchor it, and persist the settlement.
     *
     * Called by scheduler or manual endpoint.
     */
    @Transactional
    public Settlement aggregateAndAnchorSettlement() {
        // 1) Find transfers eligible for settlement:
        // Completed but not yet ANCHORED
        List<Transfer> completed = transferRepository.findByState(TransferState.COMPLETED);

        // Filter out ones already anchored (if your transfers can have state ANCHORED or other markers)
        List<Transfer> toSettle = completed.stream()
                .filter(t -> t.getState() != TransferState.ANCHORED) // safety check (most will be COMPLETED)
                .sorted(Comparator.comparing(Transfer::getUpdatedAt))   // deterministic order
                .collect(Collectors.toList());

        if (toSettle.isEmpty()) {
            return null; // nothing to do
        }

        // 2) Build settlement summary (for example: concat transfer ids + amounts)
        String payload = toSettle.stream()
                .map(t -> String.format("%s|%s|%s", t.getId(), t.getAmount().toPlainString(), t.getUpdatedAt()))
                .collect(Collectors.joining(";"));

        // 3) Create SHA-256 of payload
        String summaryHash = DigestUtils.sha256Hex(payload);

        // 4) Save Settlement (initial)
        BigDecimal total = toSettle.stream()
                .map(Transfer::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

      Settlement settlement = new Settlement();
        settlement.setSummaryHash(summaryHash);
        settlement.setTotalAmount(total);
        settlement.setTransfersCount(toSettle.size());
        settlement.setTimestamp(LocalDateTime.now());
        settlement = settlementRepository.save(settlement);

        // 5) Anchor on blockchain (stub/service)
        var anchorResp = blockchainAnchorService.anchor(summaryHash);

        // 6) Update settlement with blockchain tx id and save
        settlement.setBlockchainTxId(anchorResp.blockchainTxId);
        settlementRepository.save(settlement);

        // 7) Transition transfers to ANCHORED and record audit entries
        for (Transfer t : toSettle) {
            try {
                // Transition transfer state: COMPLETED -> ANCHORED
                stateMachineService.transitionState(t, TransferState.ANCHORED, "SYSTEM_SETTLEMENT");
            } catch (Exception ex) {
                // don't abort entire settlement; log and continue
                // optionally: add a separate failure audit row
                auditService.recordAudit(t.getId(), t.getState(), TransferState.FAILED, "SYSTEM_SETTLEMENT_ERROR:" + ex.getMessage());
            }
        }

        return settlement;
    }
}
