// src/main/java/com/example/repository/TermCourseRepository.java
package com.segs.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.segs.demo.model.TermCourse;

public interface TermCourseRepository extends JpaRepository<TermCourse, Long> {
    List<TermCourse> findByTerm_Id(Long termId); 

    @Query("SELECT t.id FROM TermCourse t WHERE t.course.id = :crsid AND t.term.id = :trmid")
    Long findTcridByCrsidAndTrmid(@Param("crsid") Long crsid, @Param("trmid") Long trmid);

    List<TermCourse> findByTerm_IdAndUser_UserId(Long trmId, Long userId);


}
