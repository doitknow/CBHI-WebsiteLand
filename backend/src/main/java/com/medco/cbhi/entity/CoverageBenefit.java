package com.medco.cbhi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "coverage_benefits")
public class CoverageBenefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title; // e.g. "Primary Care"

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(name = "icon_name", length = 50)
    private String iconName; // e.g. "Stethoscope", "Hospital", "Baby"

    @Column(length = 60)
    private String tag; // e.g. "Full Coverage", "Essential", "Family"

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    @Column(nullable = false)
    private Boolean active = true;

    public CoverageBenefit() {}

    public CoverageBenefit(String title, String description, String iconName, String tag, Integer displayOrder, Boolean active) {
        this.title = title;
        this.description = description;
        this.iconName = iconName;
        this.tag = tag;
        this.displayOrder = displayOrder;
        this.active = active != null ? active : true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIconName() { return iconName; }
    public void setIconName(String iconName) { this.iconName = iconName; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
