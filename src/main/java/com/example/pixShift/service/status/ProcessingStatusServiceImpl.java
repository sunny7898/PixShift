package com.example.pixShift.service.status;

import com.example.pixShift.service.status.ProcessingStatusService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class ProcessingStatusServiceImpl implements ProcessingStatusService {

    private final Map<String, ProcessingStatus> statusMap = new ConcurrentHashMap<>();

    @Override
    public void initializeStatus(String requestId, int totalProducts) {
        statusMap.put(requestId, new ProcessingStatus(totalProducts));
    }

    @Override
    public void updateStatus(String requestId) {
        ProcessingStatus status = statusMap.get(requestId);
        if (status != null) status.incrementProcessed();
    }

    @Override
    public void updateStatus(String requestId, String statusMessage) {
        ProcessingStatus status = statusMap.get(requestId);
        if (status != null) status.setStatus(statusMessage);
    }

    @Override
    public void setCSVUrl(String requestId, String csvUrl) {
        ProcessingStatus status = statusMap.get(requestId);
        if (status != null) {
            status.setCsvUrl(csvUrl);
        }
    }

    @Override
    public ProcessingStatus getStatus(String requestId) {
        return statusMap.get(requestId);
    }
}
