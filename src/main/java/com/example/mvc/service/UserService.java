package com.example.mvc.service;

import com.example.mvc.model.ConnectionRequest;
import com.example.mvc.model.User;
import com.example.mvc.repository.ConnectionRequestRepository;
import com.example.mvc.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

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
    public User getUserById1(Long id)
    {
       Optional<User> optionaluser=  userRepository.findById(id);
       optionaluser.ifPresent(ou-> System.out.println(ou.getFullName()));
       return optionaluser.orElseThrow(()-> new RuntimeException("user not found"));

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
    @Transactional
    public ConnectionRequest acceptConnection(Long requestId) {
        ConnectionRequest request = connectionRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        System.out.println("✅ Found connection request for ID: " + requestId);

        User sender = request.getSender();
        User recipient = request.getRecipient();

        if (sender == null || recipient == null) {
            throw new RuntimeException("❌ Sender or recipient is null");
        }

        System.out.println("👤 Sender: " + sender.getUsername());
        System.out.println("👤 Recipient: " + recipient.getUsername());

        // Initialize connections if needed
     //   sender.initializeConnections();
     //   recipient.initializeConnections();

        Hibernate.initialize(sender.getConnections());
        Hibernate.initialize(recipient.getConnections());


        // Add connection both ways
        sender.getConnections().add(recipient);
        recipient.getConnections().add(sender);

        userRepository.save(sender);
        userRepository.save(recipient);

        request.setStatus("ACCEPTED");
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
