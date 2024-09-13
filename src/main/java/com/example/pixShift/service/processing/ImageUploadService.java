package com.example.pixShift.service.processing;

import java.io.ByteArrayOutputStream;

public interface ImageUploadService {
    public String uploadToBlobStorage(ByteArrayOutputStream imageStream, String originalImageUrl, String suffix);
}
