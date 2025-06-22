package com.segs.demo.repository;

import com.segs.demo.model.DropdownItem;
import com.segs.demo.model.Egcrstt1;
import com.segs.demo.model.Egcrstt1Id;
import com.segs.demo.model.ExamType;
import com.segs.demo.model.StudentGradeDTO;
import com.segs.demo.model.StudentGradeReportDTO;

// Import your DTO here, e.g., import com.segs.demo.dto.GradeResultDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    boolean existsById_TcridAndId_ExamtypeIdAndUpdatedByIsNotNullAndUpdatedDateIsNotNull(Long tcrid, Long examtypeId);

    @Query("SELECT new com.segs.demo.model.StudentGradeDTO(" +
       "s.stdinstid, " +           // studentId (String)
       "CONCAT(s.stdfirstname, ' ', s.stdlastname), " + // studentName (String)
       "s.stdemail, " +           // studentEmail (String) - ADDED THIS LINE
       "g.gradeValue" +           // grade (String)
       ") " +
       "FROM Egcrstt1 e " +
       "JOIN Student s ON e.id.studId = s.stdid " +
       "JOIN Grade g ON e.obtainedGradeId = g.id " +
       "WHERE e.id.tcrid = :termCourseId " +
       "AND e.id.examtypeId = :examTypeId " +
       "AND e.updatedBy IS NOT NULL AND e.updatedDate IS NOT NULL " +
       "AND s.stdrowstate > 0")
List<StudentGradeDTO> findUpdatedStudentGradesForReport(
@Param("termCourseId") Long termCourseId,
@Param("examTypeId") Long examTypeId);

    /**
     * Retrieves a list of DropdownItem for ExamTypes that have at least one
     * associated Egcrstt1 entry with grades that have been 'updated'
     * (i.e., updatedBy and updatedDate are not null) for a given TermCourse.
     */
    @Query("SELECT NEW com.segs.demo.model.DropdownItem(CAST(et.id AS string), et.title) " +
           "FROM ExamType et JOIN Egcrstt1 e ON et.id = e.id.examtypeId " +
           "WHERE e.id.tcrid = :termCourseId " +
           "AND e.updatedBy IS NOT NULL AND e.updatedDate IS NOT NULL " + // Key condition for "updated"
           "AND et.rowState > 0") // Assuming ExamType entity has 'rowstate' property
    List<DropdownItem> findExamTypesWithUpdatedGradesByTermCourseId(@Param("termCourseId") Long termCourseId);

}

