package com.ec2.main.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ec2.main.model.Eggradm1;

@Repository
public interface Eggradm1Repository extends JpaRepository<Eggradm1, Long> {

    // Use entity field name "gradLt"
     @Query("SELECT e.grad_id FROM Eggradm1 e WHERE e.grad_lt = :gradeValue")
    Long findGradeIdByValue(@Param("gradeValue") String gradeValue);

    @Query("SELECT e.grad_pt FROM Eggradm1 e WHERE e.grad_id = :grad_id")
    BigDecimal findGradPtByGradId(@Param("grad_id") Long grad_id);

    @Query("SELECT e.grad_id, e.grad_lt FROM Eggradm1 e")
    List<Object[]> getGradeIdToLetterMap();

    List<Eggradm1> findByRowstateGreaterThan(short rowState);

    // Native query must use real table/column names
    @Query("SELECT eg.grad_lt FROM Eggradm1 eg WHERE eg.grad_id = :obtgr AND eg.rowstate > 0")
    String getGrade(@Param("obtgr") Long obtgr);

}
