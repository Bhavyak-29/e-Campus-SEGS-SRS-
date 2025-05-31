package com.segs.demo.repository;

import com.segs.demo.model.StudentGrade;
import com.segs.demo.model.StudentGradeDTO;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface StudentGradeRepository extends JpaRepository<StudentGrade, Long>{

    @Query("SELECT new com.segs.demo.model.StudentGradeDTO(s.id, s.name, '', g.gradeValue) " +
       "FROM Grade g " +
       "JOIN g.enrollment e " +
       "JOIN e.student s " +
       "JOIN e.course tc " +
       "WHERE tc.id = :CRSID AND g.examType = :examTypeId")
List<StudentGradeDTO> findByCRSIDAndExamTypeId(@Param("CRSID") Long CRSID,
                                                      @Param("examTypeId") Long examTypeId);


}
