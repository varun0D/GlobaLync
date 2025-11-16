package com.CodeBlooded.GlobaLyncBackend.integration.psp;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class PspService {

    // Supported currencies
    private static final List<String> SUPPORTED_CURRENCIES = List.of("INR", "USD", "EUR");

    // Fake PSP Payment API
    public PspPaymentResponse initiatePayment(String senderId, BigDecimal amount, String currency) {

        // RULE 1: Block sender
        if ("BLOCKED".equalsIgnoreCase(senderId)) {
            return new PspPaymentResponse(false, null, "SENDER_BLOCKED");
        }

        // RULE 2: Currency check
        if (!SUPPORTED_CURRENCIES.contains(currency.toUpperCase())) {
            return new PspPaymentResponse(false, null, "UNSUPPORTED_CURRENCY");
        }

        // RULE 3: Amount limit
        if (amount.compareTo(BigDecimal.valueOf(50000)) > 0) {
            return new PspPaymentResponse(false, null, "AMOUNT_LIMIT_EXCEEDED");
        }

        // If all good → accept
        String paymentId = UUID.randomUUID().toString();
        return new PspPaymentResponse(true, paymentId, "ACCEPTED");
    }

    // Fake PSP payout API
    public String initiatePayout(String receiverId, BigDecimal amount) {

        // RULE 1: Blacklisted receiver
        if ("BLACKLISTED".equalsIgnoreCase(receiverId)) {
            return null; // means failed
        }

        // RULE 2: Large payout rejection
        if (amount.compareTo(BigDecimal.valueOf(100000)) > 0) {
            return null;
        }

        // Everything valid → accept payout
        return "PAYOUT-" + UUID.randomUUID();
    }
}
