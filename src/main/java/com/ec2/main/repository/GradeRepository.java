package com.ec2.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ec2.main.model.Grade;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    // Finds all grades by termCourseId and examType
    //List<Grade> findByEnrollment_Course_IdAndExamType(Long CRSID, Long examType);
    @Query("SELECT g FROM Grade g WHERE g.enrollment.course.crsid = :crsid AND g.examType = :examType")
    List<Grade> findByCourseAndExamType(@Param("crsid") Long crsid, @Param("examType") Long examType);

}
