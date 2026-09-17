package com.medco.cbhi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "media_assets")
public class MediaAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String filename;

    @Column(name = "original_name", length = 255)
    private String originalName;

    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;

    @Column(name = "content_type", length = 100)
    private String contentType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(length = 60)
    private String category; // e.g. "partners", "benefits", "hero", "badges", "general"

    @Column(name = "alt_text", length = 255)
    private String altText;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt = LocalDateTime.now();

    public MediaAsset() {}

    public MediaAsset(String filename, String originalName, String fileUrl, String contentType, Long fileSize, String category, String altText) {
        this.filename = filename;
        this.originalName = originalName;
        this.fileUrl = fileUrl;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.category = category;
        this.altText = altText;
        this.uploadedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getOriginalName() { return originalName; }
    public void setOriginalName(String originalName) { this.originalName = originalName; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getAltText() { return altText; }
    public void setAltText(String altText) { this.altText = altText; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}
