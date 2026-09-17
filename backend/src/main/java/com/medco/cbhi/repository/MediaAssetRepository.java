package com.medco.cbhi.repository;

import com.medco.cbhi.entity.MediaAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaAssetRepository extends JpaRepository<MediaAsset, Long> {
    List<MediaAsset> findAllByOrderByUploadedAtDesc();
    List<MediaAsset> findByCategoryOrderByUploadedAtDesc(String category);
    Optional<MediaAsset> findByFilename(String filename);
}
