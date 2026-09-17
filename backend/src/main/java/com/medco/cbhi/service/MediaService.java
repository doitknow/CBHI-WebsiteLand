package com.medco.cbhi.service;

import com.medco.cbhi.entity.MediaAsset;
import com.medco.cbhi.repository.MediaAssetRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MediaService {

    private final Path uploadLocation;
    private final MediaAssetRepository mediaRepository;

    public MediaService(MediaAssetRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
        this.uploadLocation = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create upload directory", ex);
        }
    }

    public List<MediaAsset> getAllMedia() {
        return mediaRepository.findAllByOrderByUploadedAtDesc();
    }

    public List<MediaAsset> getMediaByCategory(String category) {
        if (category == null || category.equalsIgnoreCase("all")) {
            return getAllMedia();
        }
        return mediaRepository.findByCategoryOrderByUploadedAtDesc(category);
    }

    public MediaAsset storeFile(MultipartFile file, String category, String altText) throws IOException {
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = "";
        int extIndex = originalFilename.lastIndexOf('.');
        if (extIndex > 0) {
            extension = originalFilename.substring(extIndex);
        }

        String safeName = originalFilename.replaceAll("[^a-zA-Z0-9.-]", "_");
        String uniqueFilename = System.currentTimeMillis() + "_" + safeName;

        Path targetPath = this.uploadLocation.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        String fileUrl = "/api/public/media/" + uniqueFilename;
        String contentType = file.getContentType();
        if (contentType == null) contentType = "image/png";

        MediaAsset asset = new MediaAsset(
                uniqueFilename,
                originalFilename,
                fileUrl,
                contentType,
                file.getSize(),
                category != null && !category.isBlank() ? category : "general",
                altText != null ? altText : originalFilename
        );

        return mediaRepository.save(asset);
    }

    public Resource loadFileAsResource(String filename) {
        try {
            Path filePath = this.uploadLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
        } catch (MalformedURLException ex) {
            // file not found
        }
        return null;
    }

    public MediaAsset updateMedia(Long id, String altText, String category) {
        MediaAsset asset = mediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media not found with id: " + id));

        if (altText != null) asset.setAltText(altText);
        if (category != null) asset.setCategory(category);

        return mediaRepository.save(asset);
    }

    public void deleteMedia(Long id) {
        Optional<MediaAsset> assetOpt = mediaRepository.findById(id);
        if (assetOpt.isPresent()) {
            MediaAsset asset = assetOpt.get();
            try {
                Path filePath = this.uploadLocation.resolve(asset.getFilename()).normalize();
                Files.deleteIfExists(filePath);
            } catch (IOException ignored) {}
            mediaRepository.deleteById(id);
        }
    }

    public long count() {
        return mediaRepository.count();
    }

    public void seedExistingImage(Path sourcePath, String category, String altText) {
        try {
            if (!Files.exists(sourcePath)) return;

            String filename = sourcePath.getFileName().toString();
            if (mediaRepository.findByFilename(filename).isPresent()) return;

            Path targetPath = this.uploadLocation.resolve(filename);
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

            String probeType = Files.probeContentType(sourcePath);
            String contentType = probeType != null ? probeType : "image/png";
            long size = Files.size(sourcePath);
            String fileUrl = "/api/public/media/" + filename;

            MediaAsset asset = new MediaAsset(
                    filename,
                    filename,
                    fileUrl,
                    contentType,
                    size,
                    category,
                    altText
            );
            mediaRepository.save(asset);
        } catch (Exception ex) {
            // ignore initial seeding errors
        }
    }
}
