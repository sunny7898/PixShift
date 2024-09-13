package com.example.pixShift.service.processing;

import com.example.pixShift.model.Product;

import com.example.pixShift.service.processing.ImageProcessingService;
import com.example.pixShift.service.processing.ImageUploadService;
import com.example.pixShift.service.product.ProductService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class ImageProcessingServiceImpl implements ImageProcessingService {

    @Override
    public Map<String, ByteArrayOutputStream> processImagesForProduct(Product product) {

        List<String> imageUrls = product.getInputImageUrls();
        Map<String, ByteArrayOutputStream> imageStreams = new HashMap<>();

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (String imageUrl: imageUrls) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    ByteArrayOutputStream imageStream = downloadImage(imageUrl); // Download the image to a ByteArrayOutputStream
                    ByteArrayOutputStream compressedImageStream = compressImage(imageStream); // Compress the image

                    // Store the compressed image stream in the map
                    synchronized (imageStreams) {
                        imageStreams.put(imageUrl, compressedImageStream);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Error processing image: " + imageUrl);
                }
            });
            futures.add(future);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return imageStreams;
    }

    @Override
    public ByteArrayOutputStream downloadImage(String imageUrl) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Download the image
        try (InputStream inputStream = new URL(imageUrl).openStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            // Read from the input stream and write it to the output stream
            while ((bytesRead = inputStream.read(buffer)) != -1) outputStream.write(buffer, 0, bytesRead);
        }
        return outputStream;
    }

    private ByteArrayOutputStream compressImage(ByteArrayOutputStream imageStream) throws IOException {

        ByteArrayOutputStream compressedStream = new ByteArrayOutputStream();

        // Compress image using Thumbnailator
        Thumbnails.of(new ByteArrayInputStream(imageStream.toByteArray()))
                .scale(1) // Keep the original dimensions
                .outputQuality(0.5) // Reduce the quality by 50%
                .toOutputStream(compressedStream);

        return compressedStream;
    }
}
