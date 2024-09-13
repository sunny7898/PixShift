package com.example.pixShift.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CSVData {
    private int serialNumber;
    private String productName;
    private List<String> imageUrls;
}
