package com.CodeBlooded.GlobaLyncBackend.integration.psp;

public class PspPaymentResponse {

    private boolean accepted;
    private String pspPaymentId;
    private String reason; // why accepted/rejected

    public PspPaymentResponse(boolean accepted, String pspPaymentId, String reason) {
        this.accepted = accepted;
        this.pspPaymentId = pspPaymentId;
        this.reason = reason;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public String getPspPaymentId() {
        return pspPaymentId;
    }

    public String getReason() {
        return reason;
    }
}
