package com.medco.cbhi.service;

import com.medco.cbhi.dto.AdminOverviewDto;
import com.medco.cbhi.dto.PublicContentDto;
import com.medco.cbhi.entity.*;
import com.medco.cbhi.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContentService {

    private final ImpactMetricRepository metricRepository;
    private final CoverageBenefitRepository benefitRepository;
    private final EnrollmentStepRepository stepRepository;
    private final NewsArticleRepository newsRepository;
    private final SiteSettingRepository settingRepository;
    private final MediaAssetRepository mediaRepository;

    public ContentService(ImpactMetricRepository metricRepository,
                          CoverageBenefitRepository benefitRepository,
                          EnrollmentStepRepository stepRepository,
                          NewsArticleRepository newsRepository,
                          SiteSettingRepository settingRepository,
                          MediaAssetRepository mediaRepository) {
        this.metricRepository = metricRepository;
        this.benefitRepository = benefitRepository;
        this.stepRepository = stepRepository;
        this.newsRepository = newsRepository;
        this.settingRepository = settingRepository;
        this.mediaRepository = mediaRepository;
    }

    @Transactional(readOnly = true)
    public PublicContentDto getPublicContent() {
        Map<String, String> settingsMap = settingRepository.findAll().stream()
                .collect(Collectors.toMap(SiteSetting::getSettingKey, s -> s.getSettingValue() != null ? s.getSettingValue() : "", (a, b) -> b));

        List<ImpactMetric> metrics = metricRepository.findAllByOrderByDisplayOrderAsc();
        List<CoverageBenefit> benefits = benefitRepository.findByActiveTrueOrderByDisplayOrderAsc();
        List<EnrollmentStep> steps = stepRepository.findAllByOrderByStepNumberAsc();
        List<NewsArticle> latestNews = newsRepository.findByPublishedTrueOrderByCreatedAtDesc();

        return new PublicContentDto(settingsMap, metrics, benefits, steps, latestNews);
    }

    @Transactional(readOnly = true)
    public AdminOverviewDto getAdminOverview() {
        AdminOverviewDto overview = new AdminOverviewDto();
        overview.setTotalMetrics(metricRepository.count());
        overview.setTotalBenefits(benefitRepository.count());
        overview.setActiveBenefits(benefitRepository.findByActiveTrueOrderByDisplayOrderAsc().size());
        overview.setTotalSteps(stepRepository.count());
        overview.setTotalNews(newsRepository.count());
        overview.setPublishedNews(newsRepository.findByPublishedTrueOrderByCreatedAtDesc().size());
        overview.setTotalMedia(mediaRepository.count());
        overview.setDatabaseConnected(true);
        overview.setDatabaseVersion("PostgreSQL 16/18");
        return overview;
    }

    // Impact Metrics
    public List<ImpactMetric> getAllMetrics() {
        return metricRepository.findAllByOrderByDisplayOrderAsc();
    }

    public ImpactMetric saveMetric(ImpactMetric metric) {
        if (metric.getId() == null && metric.getMetricKey() != null) {
            metricRepository.findByMetricKey(metric.getMetricKey()).ifPresent(existing -> metric.setId(existing.getId()));
        }
        return metricRepository.save(metric);
    }

    public void deleteMetric(Long id) {
        metricRepository.deleteById(id);
    }

    // Benefits
    public List<CoverageBenefit> getAllBenefits() {
        return benefitRepository.findAllByOrderByDisplayOrderAsc();
    }

    public CoverageBenefit saveBenefit(CoverageBenefit benefit) {
        return benefitRepository.save(benefit);
    }

    public void deleteBenefit(Long id) {
        benefitRepository.deleteById(id);
    }

    // Steps
    public List<EnrollmentStep> getAllSteps() {
        return stepRepository.findAllByOrderByStepNumberAsc();
    }

    public EnrollmentStep saveStep(EnrollmentStep step) {
        if (step.getId() == null && step.getStepNumber() != null) {
            stepRepository.findByStepNumber(step.getStepNumber()).ifPresent(existing -> step.setId(existing.getId()));
        }
        return stepRepository.save(step);
    }

    public void deleteStep(Long id) {
        stepRepository.deleteById(id);
    }

    // News
    public List<NewsArticle> getAllNews() {
        return newsRepository.findAllByOrderByCreatedAtDesc();
    }

    public NewsArticle saveNews(NewsArticle article) {
        if (article.getId() != null) {
            article.setUpdatedAt(LocalDateTime.now());
        } else {
            article.setCreatedAt(LocalDateTime.now());
            article.setUpdatedAt(LocalDateTime.now());
        }
        return newsRepository.save(article);
    }

    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }

    // Settings
    public Map<String, String> getAllSettings() {
        return settingRepository.findAll().stream()
                .collect(Collectors.toMap(SiteSetting::getSettingKey, s -> s.getSettingValue() != null ? s.getSettingValue() : "", (a, b) -> b));
    }

    @Transactional
    public Map<String, String> updateSettings(Map<String, String> newSettings) {
        for (Map.Entry<String, String> entry : newSettings.entrySet()) {
            SiteSetting setting = settingRepository.findBySettingKey(entry.getKey())
                    .orElse(new SiteSetting(entry.getKey(), entry.getValue(), ""));
            setting.setSettingValue(entry.getValue());
            settingRepository.save(setting);
        }
        return getAllSettings();
    }
}
