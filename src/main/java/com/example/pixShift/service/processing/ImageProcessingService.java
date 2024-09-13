package com.example.pixShift.service.processing;

import com.example.pixShift.model.Product;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ImageProcessingService {

    public Map<String, ByteArrayOutputStream> processImagesForProduct(Product product);
    ByteArrayOutputStream downloadImage(String imageUrl) throws IOException;  // Add this method

}
