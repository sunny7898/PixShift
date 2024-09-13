package com.example.pixShift.service.processing;

import com.example.pixShift.model.Product;
import com.example.pixShift.service.product.ProductServiceImpl;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class CSVExportServiceImpl implements CSVExportService{

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Override
    public String generateAndUploadCSV() throws IOException {
        // Retrieve data from MongoDB using ProductServiceImpl
        List<Product> products = productService.getAllProducts();

        // Sort products by ID in decreasing order
        products.sort(Comparator.comparing(Product::getId).reversed());

        // Debug print the sorted list
        for (Product product : products) {
            log.debug("Product ID: " + product.getId() + ", Product Name: " + product.getProductName());
        }

        // Create a CSV in memory
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(outputStream))) {
            // Write header
            csvWriter.writeNext(new String[]{"Serial Number", "Product Name", "Input Image Urls", "Output Image Urls"});

            // Write product data
            for (Product product : products) {
                String serialNumber = product.getId().toString();
                String productName = product.getProductName();
                String inputImageUrls = String.join(",", product.getInputImageUrls());
                String outputImageUrls = String.join(",", product.getOutputImageUrls());

                csvWriter.writeNext(new String[]{serialNumber, productName, inputImageUrls, outputImageUrls});
            }
        }

        // Define the file name with timestamp
        String fileName = "output" + System.currentTimeMillis() + ".csv";

        // Upload the CSV to Azure Blob Storage
        String publicUrl = imageUploadService.uploadToBlobStorage(outputStream, fileName, "compressed");

        return publicUrl;
    }
}
