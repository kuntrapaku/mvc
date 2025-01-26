package com.example.mvc.service;

import com.example.mvc.model.Post;
import com.example.mvc.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FileStorageService fileStorageService; // For handling file uploads

    // Create a new post
    // Create a new post
    // Create a new post
    public Post createPost(String text, List<MultipartFile> files, String username) {
        Post post = new Post();
        post.setText(text);
        post.setUsername(username);

        List<String> mediaUrls = new ArrayList<>();

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                try {
                    System.out.println("Processing file: " + file.getOriginalFilename()); // Debugging log
                    String url = fileStorageService.storeFile(file); // Save file and get URL
                    mediaUrls.add(url);
                } catch (Exception e) {
                    System.err.println("Error storing file: " + file.getOriginalFilename() + " - " + e.getMessage());
                    throw new RuntimeException("Failed to store file: " + file.getOriginalFilename());
                }
            }
        }

        post.setMediaUrls(mediaUrls);

        // Save the post to the database (assuming there's a repository)
        return postRepository.save(post); // Ensure postRepository is properly injected
    }


    // Fetch all posts
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}
