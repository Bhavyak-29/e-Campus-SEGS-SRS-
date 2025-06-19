package com.segs.demo.repository;

import com.segs.demo.model.Egcrstt1;
import com.segs.demo.model.Egcrstt1Id;
import com.segs.demo.model.StudentGradeReportDTO;

// Import your DTO here, e.g., import com.segs.demo.dto.GradeResultDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Egcrstt1Repository extends JpaRepository<Egcrstt1, Egcrstt1Id> {

    @Query("SELECT DISTINCT e.id.examtypeId FROM Egcrstt1 e WHERE e.id.tcrid = :tcrid")
    List<Long> findDistinctExamTypeIdsByTcrid(@Param("tcrid") Long tcrid);

    List<Egcrstt1> findAllById_StudId(Long studId);
    
    @Query("SELECT e.obtainedGradeId FROM Egcrstt1 e WHERE e.rowStatus <> 'D'")
    List<Integer> findAllValidGradeIds();

    @Query(value = """
    SELECT g.grad_lt, et.examtype_title
    FROM ec2.egcrstt1 e
    JOIN ec2.eggradm1 g ON e.obtgr_id = g.grad_id
    JOIN ec2.egexamm1 et ON e.examtype_id = et.examtype_id
    WHERE e.stud_id = :studentId
      AND e.tcrid = :termCourseId
      AND e.row_st <> 'D'
      AND et.row_st > 0
    ORDER BY e.examtype_id DESC
    """, nativeQuery = true)
    List<Object[]> findGradeAndExamTitle(@Param("studentId") Long studentId, @Param("termCourseId") Long termCourseId);

    boolean existsById_TcridAndId_ExamtypeIdAndRowStatusGreaterThan(Long tcrid, Long examtypeId,String rowState);

    // Method to fetch student grades for the report
    @Query("SELECT NEW com.segs.demo.model.StudentGradeReportDTO(s.stdinstid, CONCAT(s.stdfirstname, ' ', s.stdlastname), gm.grad_lt) " +
           "FROM Egcrstt1 e JOIN Student s ON e.id.studId = s.stdid " +
           "JOIN Eggradm1 gm ON e.obtainedGradeId = gm.grad_id " + 
           "WHERE e.id.tcrid = :termCourseId AND e.id.examtypeId = :examTypeId AND e.rowStatus = '1' " + 
           "AND s.stdrowstate > 0 ORDER BY s.stdinstid")
    List<StudentGradeReportDTO> findStudentGradesForReport(@Param("termCourseId") Long termCourseId, @Param("examTypeId") Long examTypeId);
}