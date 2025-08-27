package com.ec2.main.repository;

import com.ec2.main.model.Semester;
import com.ec2.main.model.StudentRegistrations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

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

    
   @Query(value = "SELECT * FROM ec2.semesters WHERE strtrmid = :termId", nativeQuery = true)
    List<Semester> findSemestersByTerm(@Param("termId") Long termId);

}

 