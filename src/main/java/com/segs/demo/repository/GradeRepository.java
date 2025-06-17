package com.segs.demo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.segs.demo.model.Grade;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    // Finds all grades by termCourseId and examType
    List<Grade> findByEnrollment_Course_IdAndExamType(Long CRSID, Long examType);
    
}
