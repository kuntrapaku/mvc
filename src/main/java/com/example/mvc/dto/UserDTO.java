package com.example.mvc.dto;

public class UserDTO {
    private Long id;
    private String username;
    private String fullName;
    private String profilePictureUrl;
    private String bio;
    private String location;

    public UserDTO(Long id, String username, String fullName, String profilePictureUrl) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.profilePictureUrl = profilePictureUrl;
    }

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
