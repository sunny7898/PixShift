package com.example.pixShift.service.processing;

import com.example.pixShift.model.Product;
import com.example.pixShift.repository.ProductRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CSVServiceImpl implements CSVService {

    private static final Logger logger = LoggerFactory.getLogger(CSVServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> parseAndValidateCSV(MultipartFile csvFile) {
        List<Product> products = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(csvFile.getInputStream()));
            String[] values;

            // Read the first line - header
            reader.readNext();
            // Process each line in the CSV
            while ((values = reader.readNext()) != null) {
                logger.info("Serial Number: {}, Product Name: {}, InputImageUrls: {}",values[0], values[1], values[2]);

                // Validate each row
                if (isValidCSVRow(values)) {
                    // Convert row into Product Object
                    Product product = mapRowToProduct(values);
                    products.add(product);
                } else {
                    throw new IllegalArgumentException("Invalid CSV Row format: " + String.join(",", values));
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Error reading CSV File", e);
        }
        return products;
    }

    // Check if a row is valid
    private boolean isValidCSVRow(String[] values) {

        // Ensure we have exactly 3 columns
        if (values.length != 3) return false;

        // Ensure serial number is valid
        try {
            Long.parseLong(values[0]);
        } catch (NumberFormatException e){
            return false;
        }

        // Ensure product name is not empty
        if (values[1].isEmpty()) return false;

        // Ensure input image URLs are provided (not empty)
        return !values[2].isEmpty();
    }

    private Product mapRowToProduct(String[] values) {
        Product product = new Product();
        product.setId(Long.parseLong(values[0])); // Assuming the ID is in the first column of the CSV
        product.setProductName(values[1]);        // Assuming the product name is in the second column
        product.setInputImageUrls(Arrays.asList(values[2].split(","))); // Assuming input URLs are comma-separated
        return product;
    }
}
