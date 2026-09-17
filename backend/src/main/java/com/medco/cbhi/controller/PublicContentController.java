package com.medco.cbhi.controller;

import com.medco.cbhi.dto.ApiResponse;
import com.medco.cbhi.dto.PublicContentDto;
import com.medco.cbhi.service.ContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class PublicContentController {

    private final ContentService contentService;

    public PublicContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping("/content")
    public ResponseEntity<ApiResponse<PublicContentDto>> getPublicContent() {
        PublicContentDto content = contentService.getPublicContent();
        return ResponseEntity.ok(ApiResponse.ok(content));
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> healthCheck() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("system", "CBHI Backend");
        status.put("database", "PostgreSQL");
        status.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(ApiResponse.ok(status));
    }
}
