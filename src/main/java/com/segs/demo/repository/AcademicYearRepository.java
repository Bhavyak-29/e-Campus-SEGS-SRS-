package com.segs.demo.repository;

import com.segs.demo.model.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {
    
    List<AcademicYear> findByRowStateGreaterThan(int rowState);

    List<AcademicYear> findByRowStateGreaterThanOrderByField1Asc(int rowState);
}
