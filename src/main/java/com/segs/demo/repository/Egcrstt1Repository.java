package com.segs.demo.repository;

import com.segs.demo.model.Egcrstt1;
import com.segs.demo.model.Egcrstt1Id;
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

    @Query(value = "SELECT g.grad_lt, et.examtype_title " +
                   "FROM ec2.egcrstt1 e " +
                   "JOIN ec2.eggradm1 g ON e.obtgr_id = g.grad_id " +
                   "JOIN ec2.egexamm1 et ON e.examtype_id = et.examtype_id " +
                   "WHERE e.id.studId = :studentId AND e.id.tcrid = :termCourseId " +
                   "AND e.rowStatus > '0' AND et.row_st > 0 " + 
                   "ORDER BY e.id.examtypeId DESC", nativeQuery = true)
    List<Object[]> findGradeAndExamTitle(@Param("studentId") String studentId, @Param("termCourseId") Long termCourseId);
}