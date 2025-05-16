package com.example.mvc.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AIClientService {

    private final String agentUrl = "http://localhost:5000/ask"; // Python AI API

    public String sendPromptToAgent(String prompt) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Prepare the request payload
            Map<String, String> body = new HashMap<>();
            body.put("prompt", prompt);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

            // Call Python Agent API
            ResponseEntity<Map> response = restTemplate.exchange(
                    agentUrl,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (String) response.getBody().get("response");
            } else {
                return "[ERROR] No valid response from AI agent.";
            }

        } catch (Exception e) {
            return "[ERROR] " + e.getMessage();
        }
    }
}
