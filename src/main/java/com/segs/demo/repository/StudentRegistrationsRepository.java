package com.segs.demo.repository;

import com.segs.demo.model.StudentRegistrations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentRegistrationsRepository extends JpaRepository<StudentRegistrations, Long> {

    // --- New method added based on original SQL query ---
    /**
     * Corresponds to: SELECT ... FROM STUDENTREGISTRATIONS, SEMESTERS WHERE SRGROWSTATE > 0 AND SRGSTDID = ? ... ORDER BY STRSEQNO
     */
    @Query("SELECT sr FROM StudentRegistrations sr JOIN sr.semester s " +
    "WHERE sr.student.stdid = :studentId " +
    "ORDER BY s.strseqno ASC")
    List<StudentRegistrations> findAllRegistrationsByStudentIdOrderBySemesterSequence(@Param("studentId") Long studentId);

    
}

 