package com.medco.cbhi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "enrollment_steps")
public class EnrollmentStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "step_number", nullable = false)
    private Integer stepNumber; // 1, 2, 3

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(name = "badge_text", length = 120)
    private String badgeText;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    public EnrollmentStep() {}

    public EnrollmentStep(Integer stepNumber, String title, String description, String badgeText, Integer displayOrder) {
        this.stepNumber = stepNumber;
        this.title = title;
        this.description = description;
        this.badgeText = badgeText;
        this.displayOrder = displayOrder;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getStepNumber() { return stepNumber; }
    public void setStepNumber(Integer stepNumber) { this.stepNumber = stepNumber; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBadgeText() { return badgeText; }
    public void setBadgeText(String badgeText) { this.badgeText = badgeText; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
}
