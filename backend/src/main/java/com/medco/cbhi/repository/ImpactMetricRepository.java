package com.medco.cbhi.repository;

import com.medco.cbhi.entity.ImpactMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImpactMetricRepository extends JpaRepository<ImpactMetric, Long> {
    List<ImpactMetric> findAllByOrderByDisplayOrderAsc();
    Optional<ImpactMetric> findByMetricKey(String metricKey);
}
