package com.example.mvc.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
            // Validate that the file is not empty
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            // Resolve the upload directory
            Path uploadPath = Paths.get(uploadDir);

            // Create the directory if it doesn't exist
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("Created directory: " + uploadPath.toString());
            }

            // Generate a unique file name
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // Resolve the file path
            Path filePath = uploadPath.resolve(fileName);
            System.out.println("Saving file to: " + filePath.toString());

            // Save the file using Files.copy instead of file.transferTo
            Files.copy(file.getInputStream(), filePath);

            System.out.println("File saved successfully at: " + filePath.toString());
            return "/uploads/" + fileName; // Return the relative file path or URL
        } catch (IOException e) {
            e.printStackTrace(); // Print the error stack trace for debugging
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }
    }
}
