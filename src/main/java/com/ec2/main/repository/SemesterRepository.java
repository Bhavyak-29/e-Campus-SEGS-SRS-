package com.ec2.main.repository;

import com.ec2.main.model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {

    /**
     * Finds an active semester by its ID. This can be used to retrieve the
     * semester name as requested by the original query:
     * `select strname from semesters where strrowstate > 0 and strid = ?`
     *
     * In your service layer, you would call this method and then use .getStrname().
     */
    Optional<Semester> findByStridAndStrrowstateGreaterThan(Long strid, int rowState);

     @Query("SELECT s FROM Semester s JOIN Batch b ON s.batch.bchid = b.bchid JOIN Program p ON b.program.prgid = p.prgid " +
           "WHERE s.strrowstate > 0 AND b.bchrowstate > 0 AND p.prgrowstate > 0 " +
           "ORDER BY p.prgfield1 ASC, b.bchfield1 ASC, s.strseqno ASC")
    List<Semester> findAllActiveSemestersOrderedByProgramBatchAndSequence();

    // For AJAX filtering by Batch
    List<Semester> findByBatch_BchidAndStrrowstateGreaterThanOrderByStrseqnoAsc(Long bchId, int rowState);

}