package com.CodeBlooded.GlobaLyncBackend.controllers;

import com.CodeBlooded.GlobaLyncBackend.entities.jpa.Settlement;
import com.CodeBlooded.GlobaLyncBackend.services.SettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/settlement")
public class SettlementController {

    @Autowired
    private SettlementService settlementService;

    @PostMapping("/run")
    public ResponseEntity<?> runSettlement() {
        Settlement settlement = settlementService.aggregateAndAnchorSettlement();

        if (settlement == null) {
            return ResponseEntity.ok("No transfers eligible for settlement");
        }

        return ResponseEntity.ok(settlement);
    }
}
