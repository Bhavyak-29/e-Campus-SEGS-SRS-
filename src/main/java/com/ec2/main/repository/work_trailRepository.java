package com.ec2.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ec2.main.model.work_trail;

@Repository
public interface work_trailRepository extends JpaRepository<work_trail, Long> {
    @Query("SELECT MAX(w.work_id) FROM work_trail w")
    Long findMaxId();

    @Query("SELECT w FROM work_trail w WHERE w.employee_id = :employeeId AND w.work_type_code = :workTypeCode")
    List<work_trail> findByEmployeeIdAndWorkTypeCode(Long employeeId, Long workTypeCode);

    @Query("SELECT w FROM work_trail w WHERE w.work_id = :workid AND w.work_type_code = :workTypeCode")
    List<work_trail> findByWorkIdAndWorkTypeCode(Long workid, Long workTypeCode);

    @Query("""
        SELECT wt FROM work_trail wt 
        WHERE wt.node_number = 0 
        AND wt.work_type_code = 28 
        AND NOT EXISTS (
            SELECT wt2 FROM work_trail wt2 
            WHERE wt2.work_id = wt.work_id 
                AND wt2.node_number IN (1, 2)
        )
    """)
    List<work_trail> findPendingDeanRequests();

    // @Query("""
    //     SELECT wt FROM work_trail wt 
    //     WHERE wt.node_number = 1 
    //     AND wt.work_type_code = 28 
    //     AND wt.response_code = 1
    //     AND NOT EXISTS (
    //         SELECT wt2 FROM work_trail wt2 
    //         WHERE wt2.work_id = wt.work_id 
    //             AND wt2.node_number = 2
    //     )
    // """)
    // List<work_trail> findPendingRegistrarRequests();

   @Query(value = """
    SELECT 
        s.stdinstid AS studentInstituteId,
        g1.grad_lt AS presentGradeLetter,
        g2.grad_lt AS newGradeLetter,
        wt.work_id AS gmdid,
        'Submitted Successfully' AS statusSubmitted,
        'Approved' AS statusDean,
        'Pending' AS statusRegistrar,
        wt.remarks AS remarks
    FROM ec2.work_trail wt
    JOIN ec2.eggrademodification m ON wt.work_id = m.gmdid
    JOIN ec2.students s ON m.gmdstdid = s.stdid
    JOIN ec2.eggradm1 g1 ON m.gmdpresentgrade = g1.grad_id
    JOIN ec2.eggradm1 g2 ON m.gmdnewgrade = g2.grad_id
    WHERE wt.node_number = 1
      AND wt.work_type_code = 28
      AND wt.response_code = 1
      AND NOT EXISTS (
          SELECT 1 FROM ec2.work_trail wt2 
          WHERE wt2.work_id = wt.work_id 
            AND wt2.node_number = 2
      )
""", nativeQuery = true)
List<Object[]> findPendingRegistrarRequestsNative();



    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN TRUE ELSE FALSE END FROM work_trail w WHERE w.work_id = :workId AND w.node_number IN :nodeNumbers")
    boolean existsByWorkIdAndNodeNumberIn(@Param("workId") Long workId, @Param("nodeNumbers") List<Long> nodeNumbers);
}
