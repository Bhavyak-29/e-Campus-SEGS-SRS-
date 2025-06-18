package com.segs.demo.repository;

import com.segs.demo.model.StudentSemesterResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // Important for dynamic queries
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentSemesterResultRepository extends
        JpaRepository<StudentSemesterResult, Long>,
        JpaSpecificationExecutor<StudentSemesterResult>, // Keep this for standard Specification-based queries that return StudentSemesterResult entities
        CustomStudentSemesterResultRepository // Extend your custom interface for the SPI/CPI list
{

    /**
     * Corresponds to: SELECT * FROM STUDENTSEMESTERRESULT WHERE SSRROWSTATE > 0 AND SSRSRGID = ?
     */
    List<StudentSemesterResult> findByStudentRegistration_SrgidAndRowStateGreaterThan(Long registrationId, short rowState);
}