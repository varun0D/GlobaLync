package com.CodeBlooded.GlobaLyncBackend.integration.psp;

import org.springframework.stereotype.Component;

@Component
public class PspClient {

    // Simulate PSP callback webhook listener
    public boolean confirmPayment(String pspPaymentId) {
        return pspPaymentId != null && !pspPaymentId.isBlank();
    }

    public boolean confirmPayout(String pspPayoutId) {
        return pspPayoutId != null && !pspPayoutId.isBlank();
    }
}
