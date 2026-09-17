package com.medco.cbhi.repository;

import com.medco.cbhi.entity.CoverageBenefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoverageBenefitRepository extends JpaRepository<CoverageBenefit, Long> {
    List<CoverageBenefit> findAllByOrderByDisplayOrderAsc();
    List<CoverageBenefit> findByActiveTrueOrderByDisplayOrderAsc();
}
