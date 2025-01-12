package com.example.mvc.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String uploadDir = "uploads/"; // Define upload directory

    public String storeFile(MultipartFile file) {
        try {
            // Create the directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate a unique file name
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // Save the file
            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath.toFile());

            return "/uploads/" + fileName; // Return the file URL
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}
