package com.example.mvc.dto;

public class ConnectionRequestDTO {
    private Long requestId;    // ConnectionRequest ID
    private Long senderId;     // Sender User ID
    private String senderUsername;
    private String senderFullName;
    private String senderProfilePictureUrl;

    public ConnectionRequestDTO(Long requestId, Long senderId, String senderUsername, String senderFullName, String senderProfilePictureUrl) {
        this.requestId = requestId;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.senderFullName = senderFullName;
        this.senderProfilePictureUrl = senderProfilePictureUrl;
    }

    // Getters and Setters

    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public String getSenderUsername() { return senderUsername; }
    public void setSenderUsername(String senderUsername) { this.senderUsername = senderUsername; }

    public String getSenderFullName() { return senderFullName; }
    public void setSenderFullName(String senderFullName) { this.senderFullName = senderFullName; }

    public String getSenderProfilePictureUrl() { return senderProfilePictureUrl; }
    public void setSenderProfilePictureUrl(String senderProfilePictureUrl) { this.senderProfilePictureUrl = senderProfilePictureUrl; }
}
