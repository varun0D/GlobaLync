package com.CodeBlooded.GlobaLyncBackend.dto;

public class KycRequest {

    private String receiverId;
    private String documentType;
    private String documentNumber;

    // Getters and setters
    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }

    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }

    public String getDocumentNumber() { return documentNumber; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }
}
