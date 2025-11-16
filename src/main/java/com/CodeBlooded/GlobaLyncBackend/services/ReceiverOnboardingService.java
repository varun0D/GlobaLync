package com.CodeBlooded.GlobaLyncBackend.services;

import com.CodeBlooded.GlobaLyncBackend.entities.jpa.Receiver;
import com.CodeBlooded.GlobaLyncBackend.enums.KycStatus;
import com.CodeBlooded.GlobaLyncBackend.repositories.jpa.ReceiverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ReceiverOnboardingService {

    @Autowired
    private ReceiverRepository receiverRepository;

    public Receiver create(Receiver receiver) {
        receiver.setKycStatus(KycStatus.PENDING);
        return receiverRepository.save(receiver);
    }

    public Receiver get(String id) {
        return receiverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));
    }

    public Receiver update(String id, Receiver updated) {
        Receiver r = get(id);
        r.setName(updated.getName());
        r.setPhone(updated.getPhone());
        r.setCountry(updated.getCountry());
        return receiverRepository.save(r);
    }

    public Receiver startKyc(String id) {
        Receiver r = get(id);
        r.setKycStatus(KycStatus.IN_REVIEW);
        receiverRepository.save(r);

        simulateKycApproval(r.getId());
        return r;
    }

    @Async
    public void simulateKycApproval(String receiverId) {
        try { Thread.sleep(3000); } catch (Exception ignored) {}

        Receiver r = get(receiverId);
        r.setKycStatus(KycStatus.APPROVED);
        receiverRepository.save(r);
    }
}
