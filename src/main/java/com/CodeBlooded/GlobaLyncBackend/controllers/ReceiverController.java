package com.CodeBlooded.GlobaLyncBackend.controllers;

import com.CodeBlooded.GlobaLyncBackend.dto.ClaimRequest;
import com.CodeBlooded.GlobaLyncBackend.dto.KycRequest;
import com.CodeBlooded.GlobaLyncBackend.entities.jpa.KYCRecord;
import com.CodeBlooded.GlobaLyncBackend.entities.jpa.Receipt;
import com.CodeBlooded.GlobaLyncBackend.entities.jpa.Receiver;
import com.CodeBlooded.GlobaLyncBackend.services.ReceiverOnboardingService;
import com.CodeBlooded.GlobaLyncBackend.services.ReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receiver")
public class ReceiverController {

    @Autowired
    private ReceiverService receiverService;  // existing service

    @Autowired
    private ReceiverOnboardingService onboardingService; // NEW

    // -------------------------------
    // NEW RECEIVER ONBOARDING ENDPOINTS
    // -------------------------------

    @PostMapping("/create")
    public ResponseEntity<Receiver> create(@RequestBody Receiver receiver) {
        return ResponseEntity.ok(onboardingService.create(receiver));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Receiver> get(@PathVariable String id) {
        return ResponseEntity.ok(onboardingService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Receiver> update(@PathVariable String id, @RequestBody Receiver receiver) {
        return ResponseEntity.ok(onboardingService.update(id, receiver));
    }

    @PostMapping("/{id}/start-kyc")
    public ResponseEntity<String> startKyc(@PathVariable String id) {
        onboardingService.startKyc(id);
        return ResponseEntity.ok("KYC started for receiver: " + id);
    }

    // -------------------------------
    // EXISTING CLAIM + KYC ENDPOINTS
    // -------------------------------

    @PostMapping("/kyc")
    public ResponseEntity<KYCRecord> verifyKyc(@RequestBody KycRequest req) {
        return ResponseEntity.ok(receiverService.verifyKyc(req));
    }

    @PostMapping("/claim")
    public ResponseEntity<Receipt> claim(@RequestBody ClaimRequest req) {
        return ResponseEntity.ok(receiverService.claimTransfer(req));
    }
}
