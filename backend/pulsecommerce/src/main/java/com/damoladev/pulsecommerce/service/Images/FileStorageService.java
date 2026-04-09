package com.damoladev.pulsecommerce.service.Images;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;


public interface FileStorageService {
        public Map<String, Object> uploadFile(MultipartFile file, String filePath, String filename) throws IOException;

        public Map<String, Object> deleteFile(String key, String reourceType) throws IOException;
}
