package com.example.pixShift.service;

import com.example.pixShift.model.Product;
import com.example.pixShift.service.PixShiftService;
import com.example.pixShift.service.processing.CSVExportService;
import com.example.pixShift.service.processing.CSVService;
import com.example.pixShift.service.processing.ImageProcessingService;
import com.example.pixShift.service.processing.ImageUploadService;
import com.example.pixShift.service.product.ProductService;
import com.example.pixShift.service.status.ProcessingStatus;
import com.example.pixShift.service.status.ProcessingStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class PixShiftServiceImpl implements PixShiftService {

    @Autowired
    private CSVService csvService;
    @Autowired
    private ImageProcessingService imageProcessingService;
    @Autowired
    private ImageUploadService imageUploadService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProcessingStatusService processingStatusService;
    @Autowired
    private CSVExportService csvExportService;

    @Override
    public String processCSVFile(MultipartFile file) {

        try {
            if (file.isEmpty() || !file.getContentType().equals("text/csv")) {
                throw new IllegalArgumentException("Invalid file type. Please upload a CSV file.");
            }

            // Parse and validate CSV, and get list of products
            List<Product> products = csvService.parseAndValidateCSV(file);

            // Generate a unique request ID
            String requestId = UUID.randomUUID().toString();

            // Initialize processing status
            processingStatusService.initializeStatus(requestId, products.size());

            // Process images asynchronously
            List<CompletableFuture<Void>> imageProcessingFutures = products.stream()
                .map(product -> CompletableFuture.runAsync(() -> {
                    try {
                        // Process images for each product
                        Map<String, ByteArrayOutputStream> imageStreams = imageProcessingService.processImagesForProduct(product);

                        // Process and upload each image
                        for (Map.Entry<String, ByteArrayOutputStream> entry : imageStreams.entrySet()) {
                            String imageUrl = entry.getKey();
                            ByteArrayOutputStream compressedImageStream = entry.getValue();

                            // Download and upload the original image
                            ByteArrayOutputStream originalImageStream = imageProcessingService.downloadImage(imageUrl);
                            String originalPublicUrl = imageUploadService.uploadToBlobStorage(originalImageStream, imageUrl, "original");

                            // Upload image and get public URL
                            String compressedImageUrl = imageUploadService.uploadToBlobStorage(compressedImageStream, imageUrl, "compressed");

//                            // Verify the compression
//                            verifyCompression(originalPublicUrl, compressedImageUrl);

                            // Update product with the new image URL
                            productService.updateProductWithCompressedImageUrl(compressedImageUrl, product);
                        }
                        processingStatusService.updateStatus(requestId);
                    } catch (Exception e) {
                        log.error("Error processing product: {}", product.getProductName(), e);
                        processingStatusService.updateStatus(requestId, "FAILED");
                    }
                 })).toList();

            // Wait for all image processing and uploading to complete
            CompletableFuture.allOf(imageProcessingFutures.toArray(new CompletableFuture[0])).join();

            // Generate and upload CSV after processing is complete
            String csvUrl = csvExportService.generateAndUploadCSV(); // Call the CSV export service
            processingStatusService.setCSVUrl(requestId, csvUrl); // Set the CSV URL in status

            return requestId;

        } catch (Exception e) {
            log.error("Error processing CSV file", e);
            throw new RuntimeException("Error processing CSV file.", e);
        }
    }

    private void verifyCompression(String originalImageUrl, String compressedImageUrl) throws IOException {
        try {
            // Download original image size
            long originalSize = getImageSize(originalImageUrl);
            // Download compressed image size
            long compressedSize = getImageSize(compressedImageUrl);

            // Log sizes for debugging
            log.info("Original Size: " + originalSize);
            log.info("Compressed Size: " + compressedSize);

            // Verify that the compressed image is smaller
            if (compressedSize >= originalSize) {
                throw new RuntimeException("Compression failed: Compressed image size is not smaller.");
            }
        } catch (IOException e) {
            log.error("Error verifying compression. Original URL: {}, Compressed URL: {}", originalImageUrl, compressedImageUrl, e);
            throw e;
        }
    }

    private long getImageSize(String imageUrl) throws IOException {
        try (InputStream stream = new URL(imageUrl).openStream()) {
            return stream.available();
        } catch (FileNotFoundException e) {
            log.error("File not found: {}", imageUrl);
            throw new IOException("File not found: " + imageUrl, e);
        } catch (IOException e) {
            log.error("Error accessing image: {}", imageUrl);
            throw e;
        }
    }

    @Override
    public ProcessingStatus getProcessingStatus(String requestId) {
        return processingStatusService.getStatus(requestId);
    }
}
