package com.example.mvc.payload.response;

public class ExtendedLoginResponse {
    private Long id;
    private String fullName;
    private String token;

    public ExtendedLoginResponse(Long id, String fullName, String token) {
        this.id = id;
        this.fullName = fullName;
        this.token = token;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getToken() { return token; }
}
