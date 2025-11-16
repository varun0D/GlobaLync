package com.CodeBlooded.GlobaLyncBackend.ledgers;

import com.CodeBlooded.GlobaLyncBackend.entities.jpa.PoolLedger;
import com.CodeBlooded.GlobaLyncBackend.repositories.jpa.LedgerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class LedgerService {

    @Autowired
    private LedgerRepository ledgerRepository;

    // Update sender pool balance
    public void updateSenderPool(BigDecimal amount) {
        PoolLedger ledger = ledgerRepository.findByPoolType("sender")
                .orElseGet(() -> new PoolLedger("sender", BigDecimal.ZERO, LocalDateTime.now()));
        ledger.setBalance(ledger.getBalance().add(amount));
        ledger.setLastUpdated(LocalDateTime.now());
        ledgerRepository.save(ledger);
    }

    // Update receiver pool balance
    public void updateReceiverPool(BigDecimal amount) {
        PoolLedger ledger = ledgerRepository.findByPoolType("receiver")
                .orElseGet(() -> new PoolLedger("receiver", BigDecimal.ZERO, LocalDateTime.now()));
        ledger.setBalance(ledger.getBalance().add(amount)); // pass negative to subtract
        ledger.setLastUpdated(LocalDateTime.now());
        ledgerRepository.save(ledger);
    }

    public void creditReceiverPool(BigDecimal amount) { updateReceiverPool(amount); }
    public void debitSenderPool(BigDecimal amount) { updateSenderPool(amount.negate()); }

    // Compute balances for admin
    public PoolBalancesReport computePoolBalances() {
        BigDecimal sender = ledgerRepository.findByPoolType("sender")
                .map(PoolLedger::getBalance).orElse(BigDecimal.ZERO);
        BigDecimal receiver = ledgerRepository.findByPoolType("receiver")
                .map(PoolLedger::getBalance).orElse(BigDecimal.ZERO);
        return new PoolBalancesReport(sender, receiver);
    }

    public static class PoolBalancesReport {
        private BigDecimal senderBalance;
        private BigDecimal receiverBalance;

        public PoolBalancesReport(BigDecimal senderBalance, BigDecimal receiverBalance) {
            this.senderBalance = senderBalance;
            this.receiverBalance = receiverBalance;
        }

        public BigDecimal getSenderBalance() { return senderBalance; }
        public BigDecimal getReceiverBalance() { return receiverBalance; }
    }
}
