package com.segs.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.segs.demo.model.Term;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {
    List<Term> findByAcademicYear_Id(Long academicYearId);
}