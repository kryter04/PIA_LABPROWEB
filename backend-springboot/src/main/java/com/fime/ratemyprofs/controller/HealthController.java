package com.fime.ratemyprofs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "RateMyProfs API is running");
        response.put("version", "1.0.0");
        response.put("java", System.getProperty("java.version"));
        response.put("springBoot", "3.4.0");
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> info() {
        Map<String, String> info = new HashMap<>();
        info.put("application", "RateMyProfs");
        info.put("description", "Rate My Professors API - FIME UANL");
        info.put("version", "1.0.0");
        info.put("java", System.getProperty("java.version"));
        
        return ResponseEntity.ok(info);
    }
}
