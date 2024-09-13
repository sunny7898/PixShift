package com.example.pixShift.controller;

import com.example.pixShift.service.PixShiftService;
import com.example.pixShift.service.status.ProcessingStatus;
import com.example.pixShift.service.status.ProcessingStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/pixshift")
public class PixShiftController {

    @Autowired
    private PixShiftService pixShiftService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
        try {
            String requestId = pixShiftService.processCSVFile(file);
            return ResponseEntity.ok("Processing started. Request ID: " + requestId);
        } catch (Exception e) {
            log.error("Error processing CSV File", e);
            return ResponseEntity.status(500).body("Error processing CSV file.");
        }
    }

    @GetMapping("/status/{requestId}")
    public ResponseEntity<String> getStatus(@PathVariable String requestId) {
        try {
            // Retrieve and return the status of the processing based on requestId
            ProcessingStatus status = pixShiftService.getProcessingStatus(requestId);
            if (status == null) {
                return ResponseEntity.status(404).body("Status not found for request ID: " + requestId);
            }
            String responseMessage = String.format("Processing status: %s\nCSV URL: %s",
                    status.getStatus(), status.getCsvUrl());
            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            log.error("Error retrieving processing status", e);
            return ResponseEntity.status(500).body("Error retrieving processing status.");
        }
    }
}
