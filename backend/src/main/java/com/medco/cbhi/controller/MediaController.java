package com.medco.cbhi.controller;

import com.medco.cbhi.dto.ApiResponse;
import com.medco.cbhi.entity.MediaAsset;
import com.medco.cbhi.service.MediaService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    // --- Admin Endpoints ---
    @GetMapping("/admin/media")
    public ResponseEntity<ApiResponse<List<MediaAsset>>> getMedia(
            @RequestParam(value = "category", required = false) String category) {
        List<MediaAsset> assets = mediaService.getMediaByCategory(category);
        return ResponseEntity.ok(ApiResponse.ok(assets));
    }

    @PostMapping(value = "/admin/media/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<MediaAsset>> uploadMedia(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "category", required = false, defaultValue = "general") String category,
            @RequestParam(value = "altText", required = false) String altText) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Please select a valid image file."));
        }

        try {
            MediaAsset saved = mediaService.storeFile(file, category, altText);
            return ResponseEntity.ok(ApiResponse.ok("Image uploaded successfully", saved));
        } catch (IOException ex) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Failed to store file: " + ex.getMessage()));
        }
    }

    @PutMapping("/admin/media/{id}")
    public ResponseEntity<ApiResponse<MediaAsset>> updateMedia(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String altText = body.get("altText");
        String category = body.get("category");
        MediaAsset updated = mediaService.updateMedia(id, altText, category);
        return ResponseEntity.ok(ApiResponse.ok("Media updated", updated));
    }

    @DeleteMapping("/admin/media/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMedia(@PathVariable Long id) {
        mediaService.deleteMedia(id);
        return ResponseEntity.ok(ApiResponse.ok("Media deleted", null));
    }

    // --- Public Streaming Endpoint ---
    @GetMapping("/public/media/{filename:.+}")
    public ResponseEntity<Resource> serveMedia(@PathVariable String filename) {
        Resource resource = mediaService.loadFileAsResource(filename);
        if (resource == null) {
            return ResponseEntity.notFound().build();
        }

        String contentType = null;
        try {
            contentType = Files.probeContentType(Paths.get(resource.getFile().getAbsolutePath()));
        } catch (IOException ignored) {}

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=86400")
                .body(resource);
    }
}
