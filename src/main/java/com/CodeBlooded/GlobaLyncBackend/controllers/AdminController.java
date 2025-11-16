package com.CodeBlooded.GlobaLyncBackend.controllers;

import com.CodeBlooded.GlobaLyncBackend.entities.jpa.Transfer;
import com.CodeBlooded.GlobaLyncBackend.ledgers.LedgerService;
import com.CodeBlooded.GlobaLyncBackend.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// controllers/AdminController.java
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    LedgerService ledgerService;
    @Autowired
    TransferService transferService;

    @GetMapping("/pool-balances")
    public ResponseEntity<?> getPoolBalances() {
        LedgerService.PoolBalancesReport report = ledgerService.computePoolBalances();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/transfers")
    public ResponseEntity<?> getTransfers() {
        List<Transfer> transfers = transferService.getAllTransfers();
        return ResponseEntity.ok(transfers);
    }
}
