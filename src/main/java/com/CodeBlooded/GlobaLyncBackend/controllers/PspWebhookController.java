package com.CodeBlooded.GlobaLyncBackend.controllers;

import com.CodeBlooded.GlobaLyncBackend.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook/psp")
public class PspWebhookController {

    @Autowired
    private TransferService transferService;

    // Payment confirmation webhook
    @PostMapping("/payment")
    public ResponseEntity<?> paymentCallback(
            @RequestParam String pspPaymentId,
            @RequestParam boolean accepted
    ) {
        transferService.handlePspPaymentCallback(pspPaymentId, accepted);
        return ResponseEntity.ok().build();
    }

    // Payout confirmation webhook
    @PostMapping("/payout")
    public ResponseEntity<?> payoutCallback(
            @RequestParam String payoutRef,
            @RequestParam boolean success
    ) {
        transferService.handlePspPayoutCallback(payoutRef, success);
        return ResponseEntity.ok().build();
    }
}
