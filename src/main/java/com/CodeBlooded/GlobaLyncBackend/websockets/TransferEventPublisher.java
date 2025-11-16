package com.CodeBlooded.GlobaLyncBackend.websockets;

import com.CodeBlooded.GlobaLyncBackend.entities.jpa.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransferEventPublisher {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void publishUpdate(Transfer transfer) {
        messagingTemplate.convertAndSend(
                "/topic/transfers/" + transfer.getId(),
                transfer
        );
    }

    public void publishGlobalUpdate(Transfer transfer) {
        messagingTemplate.convertAndSend(
                "/topic/transfers",
                transfer
        );
    }
}
