package com.example.pixShift.service;

import com.example.pixShift.service.status.ProcessingStatus;
import org.springframework.web.multipart.MultipartFile;

public interface PixShiftService {
    public String processCSVFile(MultipartFile file);
    ProcessingStatus getProcessingStatus(String requestId);
}
