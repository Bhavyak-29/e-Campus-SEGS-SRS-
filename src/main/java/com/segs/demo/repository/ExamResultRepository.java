// src/main/java/com/example/repository/ExamResultRepository.java
package com.segs.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.segs.demo.model.ExamResult;

public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    List<ExamResult> findByTermCourse_Course_Id(Long crsId);
}
