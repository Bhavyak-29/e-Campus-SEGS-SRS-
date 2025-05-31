package com.segs.demo.repository;

import com.segs.demo.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    // Finds all grades by termCourseId and examType
    List<Grade> findByEnrollment_Course_IdAndExamType(Long CRSID, Long examType);
}
