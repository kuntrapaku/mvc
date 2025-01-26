package com.example.mvc.repository;

import com.example.mvc.model.ConnectionRequest;
import com.example.mvc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest, Long> {

    // Find pending requests for a recipient
    List<ConnectionRequest> findByRecipientAndStatus(User recipient, String status);

    // Find sent requests for a sender
    List<ConnectionRequest> findBySenderAndStatus(User sender, String status);

    // Check if a connection request already exists between sender and recipient
    Optional<ConnectionRequest> findBySenderAndRecipientAndStatus(User sender, User recipient, String status);

    // Check if a pending request exists
    boolean existsBySenderAndRecipientAndStatus(User sender, User recipient, String status);

    // Count pending requests for a recipient
    long countByRecipientAndStatus(User recipient, String status);

    // Delete connection request by sender and recipient
    void deleteBySenderAndRecipient(User sender, User recipient);

}
