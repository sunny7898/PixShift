package com.example.pixShift.service.status;

public interface ProcessingStatusService {
    public void initializeStatus(String requestId, int totalProducts) ;

    public void updateStatus(String requestId);
    public void updateStatus(String requestId, String statusMessage);
    public void setCSVUrl(String requestId, String csvUrl);

    public ProcessingStatus getStatus(String requestId);

}
