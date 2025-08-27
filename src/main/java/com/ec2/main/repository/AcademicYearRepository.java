package com.ec2.main.repository;

import com.ec2.main.model.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {
    
    List<AcademicYear> findByRowStateGreaterThan(int rowState);

    List<AcademicYear> findByRowStateGreaterThanOrderByField1Asc(int rowState);
}
