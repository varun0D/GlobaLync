package com.CodeBlooded.GlobaLyncBackend.controllers;

import com.CodeBlooded.GlobaLyncBackend.dto.TransferRequest;
import com.CodeBlooded.GlobaLyncBackend.entities.jpa.Transfer;
import com.CodeBlooded.GlobaLyncBackend.services.TransferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    @Autowired
    private TransferService transferService;

    // ----------------------------------------------------
    // CREATE TRANSFER
    // ----------------------------------------------------
    @PostMapping
    public ResponseEntity<?> createTransfer(@Valid @RequestBody TransferRequest req) {
        Transfer transfer = transferService.createTransfer(req);
        return ResponseEntity.ok(
                Map.of(
                        "transferId", transfer.getId(),
                        "state", transfer.getState()
                )
        );
    }

    // ----------------------------------------------------
    // INITIATE PSP PAYMENT
    // ----------------------------------------------------
    @PostMapping("/{id}/initiate-payment")
    public ResponseEntity<?> initiatePayment(@PathVariable String id) {
        Transfer t = transferService.initiatePayment(id);
        return ResponseEntity.ok(
                Map.of(
                        "transferId", t.getId(),
                        "pspPaymentId", t.getPspPaymentId(),
                        "state", t.getState(),
                        "pspStatus", t.getPspStatus()
                )
        );
    }

    // ----------------------------------------------------
    // TRIGGER PAYOUT
    // ----------------------------------------------------
    @PostMapping("/{id}/trigger-payout")
    public ResponseEntity<?> triggerPayout(@PathVariable String id) {
        Transfer t = transferService.triggerPayout(id);
        return ResponseEntity.ok(
                Map.of(
                        "transferId", t.getId(),
                        "payoutRef", t.getPayoutRef(),
                        "state", t.getState(),
                        "payoutStatus", t.getPayoutStatus()
                )
        );
    }
}
