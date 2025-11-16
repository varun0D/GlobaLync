package com.CodeBlooded.GlobaLyncBackend.entities.jpa;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "settlements")
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long settlementId;

    @Column(nullable = false, length = 128)
    private String summaryHash;

    @Column(precision = 24, scale = 6)
    private BigDecimal totalAmount;

    private Integer transfersCount;

    private String blockchainTxId;

    private LocalDateTime timestamp;

    public Long getSettlementId() { return settlementId; }

    public String getSummaryHash() { return summaryHash; }
    public void setSummaryHash(String summaryHash) { this.summaryHash = summaryHash; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public Integer getTransfersCount() { return transfersCount; }
    public void setTransfersCount(Integer transfersCount) { this.transfersCount = transfersCount; }

    public String getBlockchainTxId() { return blockchainTxId; }
    public void setBlockchainTxId(String blockchainTxId) { this.blockchainTxId = blockchainTxId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
