package com.example.mvc.service;

import com.example.mvc.model.ConnectionRequest;
import com.example.mvc.model.User;
import com.example.mvc.repository.ConnectionRequestRepository;
import com.example.mvc.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private ConnectionRequestRepository connectionRequestRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Search users by username
    public List<User> searchUsers(String query) {
        return userRepository.findByUsernameContainingIgnoreCase(query);
    }

    // Get user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Send connection request with validation checks
    public void sendConnectionRequest(String senderUsername, Long recipientId) {
        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        if (sender.getId().equals(recipient.getId())) {
            throw new RuntimeException("You cannot send a connection request to yourself.");
        }

        if (connectionRequestRepository.existsBySenderAndRecipientAndStatus(sender, recipient, "PENDING")) {
            throw new RuntimeException("Connection request already exists.");
        }

        ConnectionRequest request = new ConnectionRequest();
        request.setSender(sender);
        request.setRecipient(recipient);
        request.setStatus("PENDING");
        connectionRequestRepository.save(request);
    }

    // Retrieve pending connection requests for a user
    public List<ConnectionRequest> getPendingRequests(User recipient) {
        System.out.println("Recipient ID: " + recipient.getId());
        System.out.println("Fetching pending requests for user: " + recipient.getUsername());

        // Retrieve pending connection requests for the given recipient user
        List<ConnectionRequest> requests = connectionRequestRepository.findByRecipientAndStatus(recipient, "PENDING");

        System.out.println("Pending requests found: " + requests.size());

        return requests;
    }
    // Accept connection request
    @Transactional // Add this annotation
    public ConnectionRequest acceptConnection(Long requestId) {
        ConnectionRequest request = connectionRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus("ACCEPTED");

        User sender = request.getSender();
        User recipient = request.getRecipient();

        // Inside acceptConnectionRequest() after getting sender/recipient
        sender.initializeConnections();
        recipient.initializeConnections();

        System.out.println("Sender Connections: " + sender.getConnections().size());
        System.out.println("Recipient Connections: " + recipient.getConnections().size());

        // Add bidirectional connections
        sender.getConnections().add(recipient);
        recipient.getConnections().add(sender);

        // Save changes
        userRepository.save(sender);
        userRepository.save(recipient);

        return connectionRequestRepository.save(request);
    }
    // Ignore connection request
    public void ignoreConnection(Long requestId) {
        connectionRequestRepository.deleteById(requestId);
    }

    // Count the number of pending connection requests for a user
    public long countPendingRequests(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return connectionRequestRepository.countByRecipientAndStatus(user, "PENDING");
    }

    // Find user by username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
