package com.CodeBlooded.GlobaLyncBackend.entities.jpa;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pool_ledgers")
public class PoolLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ledgerId;

    private String poolType; // "sender" or "receiver"
    private BigDecimal balance;
    private LocalDateTime lastUpdated;

    // --- Default constructor (required by JPA) ---
    public PoolLedger() {}

    // --- Constructor with arguments ---
    public PoolLedger(String poolType, BigDecimal balance, LocalDateTime lastUpdated) {
        this.poolType = poolType;
        this.balance = balance;
        this.lastUpdated = lastUpdated;
    }

    // --- Getters and Setters ---
    public Long getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(Long ledgerId) {
        this.ledgerId = ledgerId;
    }

    public String getPoolType() {
        return poolType;
    }

    public void setPoolType(String poolType) {
        this.poolType = poolType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
