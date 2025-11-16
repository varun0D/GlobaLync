package com.CodeBlooded.GlobaLyncBackend.webhooks;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    // Notify receiver with a message
    public void notifyReceiver(String receiverId, String message) {
        // For now, just log to console
        System.out.printf("Notification sent to receiver %s: %s%n", receiverId, message);
    }
}
