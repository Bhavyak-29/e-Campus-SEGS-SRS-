package com.ec2.main.repository;

import com.ec2.main.model.StudentGrade;
import com.ec2.main.model.StudentGradeDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentGradeRepository extends JpaRepository<StudentGrade, Long> {

    @Query("SELECT new com.ec2.main.model.StudentGradeDTO(" +
           "s.stdinstid, " +
           "CONCAT(s.stdfirstname, ' ', s.stdlastname), " +
           "s.stdemail, " +
           "g.gradeValue) " +
           "FROM Grade g " +
           "JOIN g.enrollment e " +
           "JOIN e.student s " +
           "JOIN e.course tc " +
           "WHERE tc.id = :CRSID AND g.examType = :examTypeId")
    List<StudentGradeDTO> findByCRSIDAndExamTypeId(@Param("CRSID") Long CRSID,
                                                   @Param("examTypeId") Long examTypeId);
}

