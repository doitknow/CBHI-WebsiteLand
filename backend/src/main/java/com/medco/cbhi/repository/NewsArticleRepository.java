package com.medco.cbhi.repository;

import com.medco.cbhi.entity.NewsArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticle, Long> {
    List<NewsArticle> findAllByOrderByCreatedAtDesc();
    List<NewsArticle> findByPublishedTrueOrderByCreatedAtDesc();
}
