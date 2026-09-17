package com.medco.cbhi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "impact_metrics")
public class ImpactMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "metric_key", nullable = false, unique = true, length = 60)
    private String metricKey; // e.g. "citizens", "woredas", "facilities"

    @Column(name = "value_number", nullable = false)
    private Integer valueNumber; // e.g. 45, 980, 3500

    @Column(nullable = false, length = 10)
    private String suffix; // e.g. "M+", "+"

    @Column(name = "label_text", nullable = false, length = 150)
    private String labelText; // e.g. "Citizens Covered"

    @Column(name = "subtext", nullable = false, length = 200)
    private String subtext; // e.g. "Across all regions"

    @Column(name = "icon_name", length = 50)
    private String iconName; // e.g. "Users", "Globe", "Building2"

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    public ImpactMetric() {}

    public ImpactMetric(String metricKey, Integer valueNumber, String suffix, String labelText, String subtext, String iconName, Integer displayOrder) {
        this.metricKey = metricKey;
        this.valueNumber = valueNumber;
        this.suffix = suffix;
        this.labelText = labelText;
        this.subtext = subtext;
        this.iconName = iconName;
        this.displayOrder = displayOrder;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMetricKey() { return metricKey; }
    public void setMetricKey(String metricKey) { this.metricKey = metricKey; }

    public Integer getValueNumber() { return valueNumber; }
    public void setValueNumber(Integer valueNumber) { this.valueNumber = valueNumber; }

    public String getSuffix() { return suffix; }
    public void setSuffix(String suffix) { this.suffix = suffix; }

    public String getLabelText() { return labelText; }
    public void setLabelText(String labelText) { this.labelText = labelText; }

    public String getSubtext() { return subtext; }
    public void setSubtext(String subtext) { this.subtext = subtext; }

    public String getIconName() { return iconName; }
    public void setIconName(String iconName) { this.iconName = iconName; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
}
