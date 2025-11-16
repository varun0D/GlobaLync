package com.CodeBlooded.GlobaLyncBackend.scheduler;

import com.CodeBlooded.GlobaLyncBackend.services.SettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// scheduler/SettlementScheduler.java
@Component
public class SettlementScheduler {

    @Autowired
    SettlementService settlementService;

    @Scheduled(cron = "0 0 * * * *") // Every hour
    public void runSettlementJob() {
        settlementService.aggregateAndAnchorSettlement();
    }
}
