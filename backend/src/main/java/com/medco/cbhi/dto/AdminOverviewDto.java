package com.medco.cbhi.dto;

import java.util.List;

public class AdminOverviewDto {
    private long totalMetrics;
    private long totalBenefits;
    private long activeBenefits;
    private long totalSteps;
    private long totalNews;
    private long publishedNews;
    private long totalMedia;
    private boolean databaseConnected;
    private String databaseVersion;

    public AdminOverviewDto() {}

    public long getTotalMedia() { return totalMedia; }
    public void setTotalMedia(long totalMedia) { this.totalMedia = totalMedia; }

    public long getTotalMetrics() { return totalMetrics; }
    public void setTotalMetrics(long totalMetrics) { this.totalMetrics = totalMetrics; }

    public long getTotalBenefits() { return totalBenefits; }
    public void setTotalBenefits(long totalBenefits) { this.totalBenefits = totalBenefits; }

    public long getActiveBenefits() { return activeBenefits; }
    public void setActiveBenefits(long activeBenefits) { this.activeBenefits = activeBenefits; }

    public long getTotalSteps() { return totalSteps; }
    public void setTotalSteps(long totalSteps) { this.totalSteps = totalSteps; }

    public long getTotalNews() { return totalNews; }
    public void setTotalNews(long totalNews) { this.totalNews = totalNews; }

    public long getPublishedNews() { return publishedNews; }
    public void setPublishedNews(long publishedNews) { this.publishedNews = publishedNews; }

    public boolean isDatabaseConnected() { return databaseConnected; }
    public void setDatabaseConnected(boolean databaseConnected) { this.databaseConnected = databaseConnected; }

    public String getDatabaseVersion() { return databaseVersion; }
    public void setDatabaseVersion(String databaseVersion) { this.databaseVersion = databaseVersion; }
}
