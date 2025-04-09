package com.example.mvc.controller;

import com.example.mvc.dto.ApiResponse;
import com.example.mvc.dto.LoginResponse;
import com.example.mvc.dto.UserDTO;
import com.example.mvc.model.ConnectionRequest;
import com.example.mvc.model.Post;
import com.example.mvc.model.Role;
import com.example.mvc.model.User;
import com.example.mvc.repository.ConnectionRequestRepository;
import com.example.mvc.service.AuthService;
import com.example.mvc.service.PostService;
import com.example.mvc.service.RoleService;
import com.example.mvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private UserService userService;

    @Autowired
    private ConnectionRequestRepository connectionRequestRepository;


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
    // Search users by query (username)



    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(
            @RequestParam String query,
            @RequestHeader("Authorization") String token) {
        String username = authService.extractUsernameFromToken(token);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        List<User> users = userService.searchUsers(query);
        List<UserDTO> userDTOs = users.stream()
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getFullName(), user.getProfilePictureUrl()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDTOs);
    }


    // Get user by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    // Send connection request
    // Send a connection request
    @PostMapping("/connections/send")
    public ResponseEntity<?> sendConnectionRequest(
            @RequestParam Long recipientId,
            @RequestHeader("Authorization") String token) {
        try {
            System.out.println("Recipient ID received: " + recipientId);
            String senderUsername = authService.extractUsernameFromToken(token);
            userService.sendConnectionRequest(senderUsername, recipientId);
            return ResponseEntity.ok(new ApiResponse(true, "Connection request sent successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        }
    }

    // Get pending connection requests for logged-in user
    @GetMapping("/connections/requests")
    public ResponseEntity<?> getPendingRequests(@RequestHeader("Authorization") String token) {
        // Extract the currently logged-in user's username (recipient: 'ayaan' who should see invitations)
        String username = authService.extractUsernameFromToken(token);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");
        }

        System.out.println("Logged-in user checking invitations: " + username);

        try {
            // Find the logged-in user (recipient who should see the invitations)
            User recipient = userService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Recipient user not found"));

            // Fetch pending connection requests where the recipient is the logged-in user
            List<ConnectionRequest> requests = userService.getPendingRequests(recipient);

            if (requests.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());  // Return empty list instead of a string message
            }

            // Convert requests to DTOs for cleaner frontend consumption
            List<UserDTO> pendingRequests = requests.stream()
                    .map(req -> new UserDTO(
                            req.getSender().getId(),
                            req.getSender().getUsername(),
                            req.getSender().getFullName(),
                            req.getSender().getProfilePictureUrl()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(pendingRequests);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving connection requests: " + e.getMessage());
        }
    }

    // Accept a connection request

    @PostMapping("/connections/accept/{requestId}")
    public ResponseEntity<?> acceptConnectionRequest(
            @PathVariable Long requestId,
            @RequestHeader("Authorization") String token) {
        String username = authService.extractUsernameFromToken(token);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false, "Unauthorized"));
        }

        // Accept the connection and get recipient's name
        ConnectionRequest acceptedRequest = userService.acceptConnection(requestId);

        User sender = acceptedRequest.getSender();
        User recipient = acceptedRequest.getRecipient();

        // Determine which user is the current user
        User connectedUser = sender.getUsername().equals(username) ? recipient : sender;

        // Prepare DTO for response
        UserDTO connectedUserDTO = new UserDTO(
                connectedUser.getId(),
                connectedUser.getUsername(),
                connectedUser.getFullName(),
                connectedUser.getProfilePictureUrl()
        );

        return ResponseEntity.ok(new ApiResponse<>(true, connectedUserDTO.getUsername() + " is now a connection.", connectedUserDTO));
    }
    @GetMapping("/connections")
    public ResponseEntity<List<UserDTO>> getConnections(@RequestHeader("Authorization") String token) {
        String username = authService.extractUsernameFromToken(token);
        User currentUser = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch connections where the user is either sender or recipient
        List<ConnectionRequest> sent = connectionRequestRepository.findBySenderAndStatus(currentUser, "ACCEPTED");
        List<ConnectionRequest> received = connectionRequestRepository.findByRecipientAndStatus(currentUser, "ACCEPTED");

        // Combine both and map the other user (not current user) to DTO
        List<UserDTO> connections = new ArrayList<>();

        for (ConnectionRequest req : sent) {
            User other = req.getRecipient();
            connections.add(new UserDTO(other.getId(), other.getUsername(), other.getFullName(), other.getProfilePictureUrl()));
        }

        for (ConnectionRequest req : received) {
            User other = req.getSender();
            connections.add(new UserDTO(other.getId(), other.getUsername(), other.getFullName(), other.getProfilePictureUrl()));
        }

        return ResponseEntity.ok(connections);
    }

    // Ignore a connection request
    @PostMapping("/connections/ignore")
    public ResponseEntity<?> ignoreConnectionRequest(
            @RequestParam Long requestId,
            @RequestHeader("Authorization") String token) {
        String username = authService.extractUsernameFromToken(token);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, "Unauthorized"));
        }
        userService.ignoreConnection(requestId);
        return ResponseEntity.ok(new ApiResponse(true, "Connection request ignored"));
    }

    @GetMapping("/users/username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserDTO dto = new UserDTO(user.getId(), user.getUsername(), user.getFullName(), user.getProfilePictureUrl());
        dto.setBio(user.getBio()); // Add this field if you have it
        dto.setLocation(user.getLocation()); // Optional
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/posts/user/{username}")
    public ResponseEntity<List<Post>> getPostsByUser(@PathVariable String username) {
        List<Post> userPosts = postService.getPostsByUsername(username);
        return ResponseEntity.ok(userPosts);
    }




}
