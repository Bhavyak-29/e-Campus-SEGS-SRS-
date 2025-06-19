package com.segs.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.segs.demo.model.Term;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {
    List<Term> findByAcademicYear_Id(Long academicYearId);
    
    @Query("SELECT DISTINCT t FROM Term t WHERE t.rowState > 0 ORDER BY t.name")
    List<Term> findDistinctByRowStateGreaterThan(int rowState);

    List<Term> findByAcademicYear_IdAndRowStateGreaterThanOrderByField1Asc(Long ayrId, int rowState);
}