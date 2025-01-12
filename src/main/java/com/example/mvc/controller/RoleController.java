package com.example.mvc.controller;

import com.example.mvc.model.Post;
import com.example.mvc.model.Role;
import com.example.mvc.model.User;
import com.example.mvc.service.AuthService;
import com.example.mvc.service.PostService;
import com.example.mvc.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5000")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PostService postService; // Inject PostService

    // --- Existing endpoints ---
    // (No changes to existing endpoints)

    // Fetch all posts
//    @GetMapping("/posts")
//    public ResponseEntity<List<Post>> getAllPosts() {
//        List<Post> posts = postService.getAllPosts();
//        return ResponseEntity.ok(posts);
//    }

    // Create a post with optional media
    @PostMapping("/posts")
    public ResponseEntity<?> createPost(
            @RequestParam String text,
            @RequestPart(required = false) List<MultipartFile> files,
            @RequestHeader("Authorization") String token // Retrieve the token
    ) {
        try {
            System.out.println("Token received: " + token);
            String username = authService.extractUsernameFromToken(token); // Extract username from the token
            System.out.println("Username extracted: " + username);
            Post newPost = postService.createPost(text, files, username); // Delegate to PostService
            return ResponseEntity.ok(newPost);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    // Get roles with optional filters
    @GetMapping("/roles")
    public List<Role> getFilteredRoles(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String industry
    ) {
        return roleService.findRolesByFilters(title, industry);
    }

    // Create a single role
    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.saveRole(role));
    }

    // Create multiple roles
    @PostMapping("roles/batch")
    public ResponseEntity<List<Role>> createRoles(@RequestBody List<Role> roles) {
        return ResponseEntity.ok(roleService.saveAllRoles(roles));
    }

    // Update a role
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role roleDetails) {
        return ResponseEntity.ok(roleService.updateRole(id, roleDetails));
    }

    // Delete a role
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok().build();
    }

    // Register a new user
    @PostMapping("/auth/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            String message = authService.registerUser(user);
            return ResponseEntity.ok(new ApiResponse(true, message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    // Login user and return token
    @PostMapping("/auth/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            String token = authService.loginUser(user);
            return ResponseEntity.ok(new LoginResponse("Login successful!", token));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new ApiResponse(false, e.getMessage()));
        }
    }

}

// DTO for generic API response
class ApiResponse {
    private boolean success;
    private String message;

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}

// DTO for login response
class LoginResponse {
    private String message;
    private String token;

    public LoginResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }
}
