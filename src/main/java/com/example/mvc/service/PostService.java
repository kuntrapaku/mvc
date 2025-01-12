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
    public Post createPost(String text, List<MultipartFile> files, String username) {
        Post post = new Post();
        post.setText(text);
        post.setUser(username);

        List<String> mediaUrls = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                String url = fileStorageService.storeFile(file); // Save file and get URL
                mediaUrls.add(url);
            }
        }

        post.setMediaUrls(mediaUrls);
        return postRepository.save(post);
    }

    // Fetch all posts
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}
