package com.ec2.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ec2.main.model.Semesters;

@Repository
public interface SemestersRepository extends JpaRepository<Semesters, Long> {
    @Query(value = "SELECT * FROM ec2.SEMESTERS sm WHERE sm.STRROWSTATE > 0 AND sm.STRBCHID = :branchId ORDER BY sm.STRSEQNO", nativeQuery = true)
    List<Semesters> findActiveSemestersByBranchId(@Param("branchId") Long branchId);

    @Query(value = "SELECT MAX(sm.STRID) FROM ec2.SEMESTERS sm WHERE sm.STRROWSTATE > 0 AND sm.STRBCHID = :batchId", nativeQuery = true)
    Long findMaxSemesterId(@Param("batchId") Long batchId);

    @Query(value = "SELECT sm.STRID FROM ec2.SEMESTERS sm WHERE sm.STRROWSTATE > 0 AND sm.STRBCHID = :batchId AND sm.STRTRMID = :termId", nativeQuery = true)
    Long findSemesterId(@Param("batchId") Long batchId, @Param("termId") Long termId);

    Optional<Semesters> findByStridAndStrrowstateGreaterThan(Long strid, int rowState);

    @Query("SELECT s FROM Semesters s JOIN Batches b ON s.batches.bchid = b.bchid JOIN Programs p ON b.programs.prgid = p.prgid " +
           "WHERE s.strrowstate > 0 AND b.bchrowstate > 0 AND p.prgrowstate > 0 " +
           "ORDER BY p.prgfield1 ASC, b.bchfield1 ASC, s.strseqno ASC")
    List<Semesters> findAllActiveSemestersOrderedByProgramBatchAndSequence();

    // For AJAX filtering by Batch
    List<Semesters> findByBatches_BchidAndStrrowstateGreaterThanOrderByStrseqnoAsc(Long bchId, int rowState);

}
