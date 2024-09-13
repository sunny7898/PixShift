package com.example.pixShift.controller;

import com.example.pixShift.response.WebhookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/webhook")
public class WebhookController {

    public ResponseEntity<String> receiveNotification(@RequestBody WebhookResponse payload) {
        // Process the webhook payload
        String status = payload.getStatus();
        String requestId = payload.getRequestId();
        String csvUrl = payload.getCsvUrl();

        // Handle the received status, requestId, and csvUrl
        // Example: log the received data or update a database
        // For now, we'll just print the data
        log.info("Received webhook notification:");
        log.info("Request ID: " + requestId);
        log.info("Status: " + status);
        log.info("CSV URL: " + csvUrl);

        // Respond with a success message
        return ResponseEntity.ok("Notification received successfully!");
    }
}
