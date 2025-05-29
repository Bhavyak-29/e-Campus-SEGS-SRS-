// src/main/java/com/example/repository/TermCourseRepository.java
package com.segs.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.segs.demo.model.TermCourse;

public interface TermCourseRepository extends JpaRepository<TermCourse, Long> {
    List<TermCourse> findByTerm_Id(Long termId); 
}
