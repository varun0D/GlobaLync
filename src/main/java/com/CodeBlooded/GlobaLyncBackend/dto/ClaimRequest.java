package com.CodeBlooded.GlobaLyncBackend.dto;

public class ClaimRequest {

    private String transferId;
    private String receiverId;

    // Getters and setters
    public String getTransferId() { return transferId; }
    public void setTransferId(String transferId) { this.transferId = transferId; }

    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }
}
