package net.pm.mailservice.mailservice.controller;

import net.pm.mailservice.mailservice.service.DeepSeekService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final DeepSeekService deepSeekService;

    public AIController(DeepSeekService deepSeekService) {
        this.deepSeekService = deepSeekService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateContent(@RequestBody String prompt) throws IOException {
        String response = deepSeekService.generateText(prompt);
        return ResponseEntity.ok(response);
    }
}