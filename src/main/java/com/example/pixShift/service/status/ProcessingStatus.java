package com.example.pixShift.service.status;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessingStatus {
    private int totalProducts;
    private int processedProducts;
    private String status = "IN_PROGRESS";
    private String csvUrl;

    public ProcessingStatus(int totalProducts) {
        this.totalProducts = totalProducts;
        this.processedProducts = 0;
        this.status = "IN_PROGRESS";
    }

    public synchronized void incrementProcessed() {
        processedProducts++;
        if (processedProducts >= totalProducts) {
            status = "COMPLETED";
        }
    }

    public void incrementProcessedCount() {
        this.processedProducts++;
    }

}
