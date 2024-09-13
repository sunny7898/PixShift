package com.example.pixShift.service.processing;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.example.pixShift.service.processing.ImageUploadService;
import com.example.pixShift.service.product.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
@AllArgsConstructor
public class ImageUploadServiceImpl implements ImageUploadService {

    private final BlobContainerClient containerClient;

    @Override
    public String uploadToBlobStorage(ByteArrayOutputStream imageStream, String originalImageUrl, String suffix) {
        // Image name manipulation
        String fileName = originalImageUrl.substring(originalImageUrl.lastIndexOf("/") + 1);
        String baseName = fileName.substring(0, fileName.lastIndexOf(".")); // Remove the file extension
        String extension = fileName.substring(fileName.lastIndexOf(".")); // Get the file extension
        // Create the new file name by appending "-output" before the file extension
        String newFileName = baseName + "-" + suffix + extension;

        BlobClient blobClient = containerClient.getBlobClient(newFileName);

        // Upload the image to blob storage
        blobClient.upload(new ByteArrayInputStream(imageStream.toByteArray()), imageStream.size(), true);

        // Generate and return the public URL of the uploaded Blob
        return blobClient.getBlobUrl();
    }
}
