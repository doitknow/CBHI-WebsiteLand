package com.medco.cbhi.repository;

import com.medco.cbhi.entity.EnrollmentStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentStepRepository extends JpaRepository<EnrollmentStep, Long> {
    List<EnrollmentStep> findAllByOrderByStepNumberAsc();
    Optional<EnrollmentStep> findByStepNumber(Integer stepNumber);
}
