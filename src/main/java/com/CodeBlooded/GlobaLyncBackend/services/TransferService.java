package com.CodeBlooded.GlobaLyncBackend.services;

import com.CodeBlooded.GlobaLyncBackend.dto.TransferRequest;
import com.CodeBlooded.GlobaLyncBackend.entities.jpa.Transfer;
import com.CodeBlooded.GlobaLyncBackend.enums.TransferState;
import com.CodeBlooded.GlobaLyncBackend.repositories.jpa.TransferRepository;
import com.CodeBlooded.GlobaLyncBackend.websockets.TransferEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.CodeBlooded.GlobaLyncBackend.enums.TransferState.*;

@Service
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private StateMachineService stateMachineService;

    @Autowired
    private TransferEventPublisher eventPublisher;

    // --------------------------------------------------------
    // CREATE TRANSFER
    // --------------------------------------------------------
    @Transactional
    public Transfer createTransfer(TransferRequest req) {

        Transfer t = new Transfer();
        t.setId(UUID.randomUUID().toString());
        t.setSenderId(req.getSenderId());
        t.setReceiverId(req.getReceiverId());
        t.setAmount(req.getAmount());
        t.setCurrency(req.getCurrency());
        t.setFxRate(req.getFxRate());

        t.setCreatedAt(LocalDateTime.now());
        t.setUpdatedAt(LocalDateTime.now());
        t.setState(CREATED);

        transferRepository.save(t);

        eventPublisher.publishGlobalUpdate(t);

        return t;
    }

    // --------------------------------------------------------
    // PSP INITIATE PAYMENT
    // Called when sender clicks "Pay Now"
    // --------------------------------------------------------
    @Transactional
    public Transfer initiatePayment(String id) {

        Transfer t = getTransfer(id);

        stateMachineService.transitionState(t, PSP_PENDING, "SENDER");

        t.setPspPaymentId("PSP-" + UUID.randomUUID());

        transferRepository.save(t);

        eventPublisher.publishUpdate(t);

        return t;
    }

    // --------------------------------------------------------
    // PSP CALLBACK (Webhook simulation)
    // PSP confirms or rejects payment
    // --------------------------------------------------------
    @Transactional
    public void handlePspPaymentCallback(String pspPaymentId, boolean accepted) {

        Transfer t = transferRepository.findByPspPaymentId(pspPaymentId)
                .orElseThrow(() -> new RuntimeException("PSP payment not found: " + pspPaymentId));

        if (accepted) {
            stateMachineService.transitionState(t, PSP_CONFIRMED, "PSP");
        } else {
            stateMachineService.transitionState(t, FAILED, "PSP");
        }

        eventPublisher.publishUpdate(t);
    }

    // --------------------------------------------------------
    // TRIGGER PAYOUT
    // Called after FX + liquidity reservation
    // --------------------------------------------------------
    @Transactional
    public Transfer triggerPayout(String id) {

        Transfer t = getTransfer(id);

        stateMachineService.transitionState(t, PAYOUT_PROCESSING, "SYSTEM");

        t.setPayoutRef("PAYOUT-" + UUID.randomUUID());

        transferRepository.save(t);

        eventPublisher.publishUpdate(t);

        return t;
    }

    // --------------------------------------------------------
    // PSP PAYOUT CALLBACK (Webhook simulation)
    // --------------------------------------------------------
    @Transactional
    public void handlePspPayoutCallback(String payoutRef, boolean success) {

        Transfer t = transferRepository.findByPayoutRef(payoutRef)
                .orElseThrow(() -> new RuntimeException("Payout reference not found: " + payoutRef));

        if (success) {
            stateMachineService.transitionState(t, PAYOUT_CONFIRMED, "PSP");
        } else {
            stateMachineService.transitionState(t, FAILED, "PSP");
        }

        eventPublisher.publishUpdate(t);
    }

    // --------------------------------------------------------
    // COMPLETE TRANSFER (Receiver claims)
    // --------------------------------------------------------
    @Transactional
    public Transfer completeTransferFlow(String id) {

        Transfer t = getTransfer(id);

        stateMachineService.transitionState(t, FX_LOCKED, "SYSTEM");
        stateMachineService.transitionState(t, LIQUIDITY_RESERVED, "SYSTEM");
        stateMachineService.transitionState(t, READY_FOR_PAYOUT, "SYSTEM");
        stateMachineService.transitionState(t, PAYOUT_PROCESSING, "PSP");
        stateMachineService.transitionState(t, PAYOUT_CONFIRMED, "PSP");
        stateMachineService.transitionState(t, CLAIMED, "RECEIVER");
        stateMachineService.transitionState(t, COMPLETED, "SYSTEM");

        eventPublisher.publishUpdate(t);

        return t;
    }

    // --------------------------------------------------------
    // Utility Methods
    // --------------------------------------------------------
    public Transfer getTransfer(String id) {
        return transferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transfer not found: " + id));
    }

    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }
    // --------------------------------------------------------
// MARK TRANSFER AS PAID (Receiver claiming)
// --------------------------------------------------------
    @Transactional
    public Transfer payTransfer(String transferId) {

        Transfer t = getTransfer(transferId);

        // If PSP already confirmed, skip
        if (t.getState() != TransferState.PSP_CONFIRMED) {
            stateMachineService.transitionState(t, TransferState.PSP_CONFIRMED, "RECEIVER");
        }

        transferRepository.save(t);
        eventPublisher.publishUpdate(t);

        return t;
    }

    // --------------------------------------------------------
// COMPLETE TRANSFER AFTER PAYOUT CLAIM
// --------------------------------------------------------
    @Transactional
    public Transfer completeTransfer(Transfer t) {

        // Apply final receiver-side transitions
        stateMachineService.transitionState(t, TransferState.CLAIMED, "RECEIVER");
        stateMachineService.transitionState(t, TransferState.COMPLETED, "SYSTEM");

        transferRepository.save(t);
        eventPublisher.publishUpdate(t);

        return t;
    }


}
