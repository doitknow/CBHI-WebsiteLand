package com.medco.cbhi.dto;

import com.medco.cbhi.entity.CoverageBenefit;
import com.medco.cbhi.entity.EnrollmentStep;
import com.medco.cbhi.entity.ImpactMetric;
import com.medco.cbhi.entity.NewsArticle;

import java.util.List;
import java.util.Map;

public class PublicContentDto {
    private Map<String, String> settings;
    private List<ImpactMetric> metrics;
    private List<CoverageBenefit> benefits;
    private List<EnrollmentStep> enrollmentSteps;
    private List<NewsArticle> latestNews;

    public PublicContentDto() {}

    public PublicContentDto(Map<String, String> settings, List<ImpactMetric> metrics,
                            List<CoverageBenefit> benefits, List<EnrollmentStep> enrollmentSteps,
                            List<NewsArticle> latestNews) {
        this.settings = settings;
        this.metrics = metrics;
        this.benefits = benefits;
        this.enrollmentSteps = enrollmentSteps;
        this.latestNews = latestNews;
    }

    public Map<String, String> getSettings() { return settings; }
    public void setSettings(Map<String, String> settings) { this.settings = settings; }

    public List<ImpactMetric> getMetrics() { return metrics; }
    public void setMetrics(List<ImpactMetric> metrics) { this.metrics = metrics; }

    public List<CoverageBenefit> getBenefits() { return benefits; }
    public void setBenefits(List<CoverageBenefit> benefits) { this.benefits = benefits; }

    public List<EnrollmentStep> getEnrollmentSteps() { return enrollmentSteps; }
    public void setEnrollmentSteps(List<EnrollmentStep> enrollmentSteps) { this.enrollmentSteps = enrollmentSteps; }

    public List<NewsArticle> getLatestNews() { return latestNews; }
    public void setLatestNews(List<NewsArticle> latestNews) { this.latestNews = latestNews; }
}
