package com.example.pixShift.service.processing;


import com.example.pixShift.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CSVService {

    public List<Product> parseAndValidateCSV(MultipartFile csvFile);

}
