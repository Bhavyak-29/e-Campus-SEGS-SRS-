package com.ec2.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ec2.main.model.Semesters;
import com.ec2.main.model.StudentRegistration;


@Repository
public interface StudentRegistrationRepository extends JpaRepository<StudentRegistration, Long> {

    // --- Existing methods (unchanged) ---
    List<StudentRegistration> findBysrgstdidAndSrgrowstateGreaterThanOrderBySrgidDesc(Long studentId, int state);
    Optional<StudentRegistration> findTopBysrgstdidOrderBySrgidDesc(Long studentId);
     @Query("SELECT sr FROM StudentRegistration sr JOIN sr.semesters s " +
    "WHERE sr.students.stdid = :studentId " +
    "ORDER BY s.strseqno ASC")
    List<StudentRegistration> findAllRegistrationsByStudentIdOrderBySemesterSequence(@Param("studentId") Long studentId);

    
    @Query(value = "SELECT * FROM ec2.semesters WHERE strtrmid = :termId", nativeQuery = true)
    List<Semesters> findSemestersByTerm(@Param("termId") Long termId);
   
}