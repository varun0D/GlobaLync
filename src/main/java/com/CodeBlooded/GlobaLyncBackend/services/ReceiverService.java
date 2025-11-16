package com.CodeBlooded.GlobaLyncBackend.services;
import com.CodeBlooded.GlobaLyncBackend.dto.ClaimRequest;
import com.CodeBlooded.GlobaLyncBackend.dto.KycRequest;
import com.CodeBlooded.GlobaLyncBackend.entities.jpa.KYCRecord;
import com.CodeBlooded.GlobaLyncBackend.entities.jpa.Receipt;
import com.CodeBlooded.GlobaLyncBackend.entities.jpa.Transfer;
import com.CodeBlooded.GlobaLyncBackend.ledgers.LedgerService;
import com.CodeBlooded.GlobaLyncBackend.repositories.jpa.KYCRecordRepository;
import com.CodeBlooded.GlobaLyncBackend.repositories.jpa.ReceiptRepository;
import com.CodeBlooded.GlobaLyncBackend.webhooks.NotificationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceiverService {

    @Autowired
    private TransferService transferService;

    @Autowired
    private LedgerService ledgerService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private KYCRecordRepository kycRecordRepository;

    @Transactional
    public Receipt claimTransfer(ClaimRequest req) {

        Transfer transfer = transferService.payTransfer(req.getTransferId());
        transferService.completeTransfer(transfer);

        Receipt receipt = new Receipt();
        receipt.setTransferId(transfer.getId());
        receipt.setReceiverId(transfer.getReceiverId());
        receipt.setAmount(transfer.getAmount());
        receipt.setCurrency(transfer.getCurrency());
        receipt.setStatus("COMPLETED");

        receiptRepository.save(receipt);

        notificationService.notifyReceiver(transfer.getReceiverId(), "TRANSFER CLAIMED");

        return receipt;
    }

    @Transactional
    public KYCRecord verifyKyc(KycRequest req) {

        KYCRecord kyc = new KYCRecord();
        kyc.setReceiverId(req.getReceiverId());
        kyc.setDocumentType(req.getDocumentType());
        kyc.setDocumentNumber(req.getDocumentNumber());
        kyc.setVerified(true);

        return kycRecordRepository.save(kyc);
    }
}
