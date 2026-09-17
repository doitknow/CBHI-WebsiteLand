package com.medco.cbhi.controller;

import com.medco.cbhi.dto.AdminOverviewDto;
import com.medco.cbhi.dto.ApiResponse;
import com.medco.cbhi.entity.CoverageBenefit;
import com.medco.cbhi.entity.EnrollmentStep;
import com.medco.cbhi.entity.ImpactMetric;
import com.medco.cbhi.entity.NewsArticle;
import com.medco.cbhi.service.ContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminContentController {

    private final ContentService contentService;

    public AdminContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<AdminOverviewDto>> getOverview() {
        return ResponseEntity.ok(ApiResponse.ok(contentService.getAdminOverview()));
    }

    // --- Impact Metrics ---
    @GetMapping("/metrics")
    public ResponseEntity<ApiResponse<List<ImpactMetric>>> getMetrics() {
        return ResponseEntity.ok(ApiResponse.ok(contentService.getAllMetrics()));
    }

    @PostMapping("/metrics")
    public ResponseEntity<ApiResponse<ImpactMetric>> createMetric(@RequestBody ImpactMetric metric) {
        return ResponseEntity.ok(ApiResponse.ok("Metric saved", contentService.saveMetric(metric)));
    }

    @PutMapping("/metrics/{id}")
    public ResponseEntity<ApiResponse<ImpactMetric>> updateMetric(@PathVariable Long id, @RequestBody ImpactMetric metric) {
        metric.setId(id);
        return ResponseEntity.ok(ApiResponse.ok("Metric updated", contentService.saveMetric(metric)));
    }

    @DeleteMapping("/metrics/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMetric(@PathVariable Long id) {
        contentService.deleteMetric(id);
        return ResponseEntity.ok(ApiResponse.ok("Metric deleted", null));
    }

    // --- Benefits ---
    @GetMapping("/benefits")
    public ResponseEntity<ApiResponse<List<CoverageBenefit>>> getBenefits() {
        return ResponseEntity.ok(ApiResponse.ok(contentService.getAllBenefits()));
    }

    @PostMapping("/benefits")
    public ResponseEntity<ApiResponse<CoverageBenefit>> createBenefit(@RequestBody CoverageBenefit benefit) {
        return ResponseEntity.ok(ApiResponse.ok("Benefit created", contentService.saveBenefit(benefit)));
    }

    @PutMapping("/benefits/{id}")
    public ResponseEntity<ApiResponse<CoverageBenefit>> updateBenefit(@PathVariable Long id, @RequestBody CoverageBenefit benefit) {
        benefit.setId(id);
        return ResponseEntity.ok(ApiResponse.ok("Benefit updated", contentService.saveBenefit(benefit)));
    }

    @DeleteMapping("/benefits/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBenefit(@PathVariable Long id) {
        contentService.deleteBenefit(id);
        return ResponseEntity.ok(ApiResponse.ok("Benefit deleted", null));
    }

    // --- Enrollment Steps ---
    @GetMapping("/enrollment-steps")
    public ResponseEntity<ApiResponse<List<EnrollmentStep>>> getSteps() {
        return ResponseEntity.ok(ApiResponse.ok(contentService.getAllSteps()));
    }

    @PostMapping("/enrollment-steps")
    public ResponseEntity<ApiResponse<EnrollmentStep>> createStep(@RequestBody EnrollmentStep step) {
        return ResponseEntity.ok(ApiResponse.ok("Step created", contentService.saveStep(step)));
    }

    @PutMapping("/enrollment-steps/{id}")
    public ResponseEntity<ApiResponse<EnrollmentStep>> updateStep(@PathVariable Long id, @RequestBody EnrollmentStep step) {
        step.setId(id);
        return ResponseEntity.ok(ApiResponse.ok("Step updated", contentService.saveStep(step)));
    }

    @DeleteMapping("/enrollment-steps/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStep(@PathVariable Long id) {
        contentService.deleteStep(id);
        return ResponseEntity.ok(ApiResponse.ok("Step deleted", null));
    }

    // --- News Articles ---
    @GetMapping("/news")
    public ResponseEntity<ApiResponse<List<NewsArticle>>> getNews() {
        return ResponseEntity.ok(ApiResponse.ok(contentService.getAllNews()));
    }

    @PostMapping("/news")
    public ResponseEntity<ApiResponse<NewsArticle>> createNews(@RequestBody NewsArticle news) {
        return ResponseEntity.ok(ApiResponse.ok("News created", contentService.saveNews(news)));
    }

    @PutMapping("/news/{id}")
    public ResponseEntity<ApiResponse<NewsArticle>> updateNews(@PathVariable Long id, @RequestBody NewsArticle news) {
        news.setId(id);
        return ResponseEntity.ok(ApiResponse.ok("News updated", contentService.saveNews(news)));
    }

    @DeleteMapping("/news/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNews(@PathVariable Long id) {
        contentService.deleteNews(id);
        return ResponseEntity.ok(ApiResponse.ok("News deleted", null));
    }

    // --- Site Settings ---
    @GetMapping("/settings")
    public ResponseEntity<ApiResponse<Map<String, String>>> getSettings() {
        return ResponseEntity.ok(ApiResponse.ok(contentService.getAllSettings()));
    }

    @PutMapping("/settings")
    public ResponseEntity<ApiResponse<Map<String, String>>> updateSettings(@RequestBody Map<String, String> settings) {
        return ResponseEntity.ok(ApiResponse.ok("Settings updated", contentService.updateSettings(settings)));
    }
}
