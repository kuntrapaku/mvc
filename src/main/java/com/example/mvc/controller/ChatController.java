package com.example.mvc.controller;

import com.example.mvc.service.AIClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
//@CrossOrigin(origins = "*") // Allow all origins (React Native needs this)
public class ChatController {

    @Autowired
    private AIClientService aiClientService;

    // 🆕 Add this inside ChatController.java (above @PostMapping)
    public static class PromptRequest {
        public String prompt;
    }

    @PostMapping("/ask")
    public String askAgent(@RequestBody PromptRequest request) {
        System.out.println("[DEBUG] ChatController called with prompt: " + request.prompt);
        return aiClientService.sendPromptToAgent(request.prompt);
    }
    @GetMapping("/health")
    public String healthCheck() {
        return "✅ /api/chat/ask is ready. Use POST with a prompt to chat.";
    }


}
