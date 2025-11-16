package com.CodeBlooded.GlobaLyncBackend.entities.jpa;

import com.CodeBlooded.GlobaLyncBackend.enums.TransferState;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String senderId;
    private String receiverId;

    private BigDecimal amount;
    private String currency;
    private BigDecimal fxRate;

    @Enumerated(EnumType.STRING)
    private TransferState state;

    // PSP Fields
    private String pspPaymentId;
    private String pspStatus;

    // Payout Fields
    private String payoutRef;
    private String payoutStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // getters + setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getFxRate() {
        return fxRate;
    }

    public void setFxRate(BigDecimal fxRate) {
        this.fxRate = fxRate;
    }

    public TransferState getState() {
        return state;
    }

    public void setState(TransferState state) {
        this.state = state;
    }

    public String getPspPaymentId() {
        return pspPaymentId;
    }

    public void setPspPaymentId(String pspPaymentId) {
        this.pspPaymentId = pspPaymentId;
    }

    public String getPspStatus() {
        return pspStatus;
    }

    public void setPspStatus(String pspStatus) {
        this.pspStatus = pspStatus;
    }

    public String getPayoutRef() {
        return payoutRef;
    }

    public void setPayoutRef(String payoutRef) {
        this.payoutRef = payoutRef;
    }

    public String getPayoutStatus() {
        return payoutStatus;
    }

    public void setPayoutStatus(String payoutStatus) {
        this.payoutStatus = payoutStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
