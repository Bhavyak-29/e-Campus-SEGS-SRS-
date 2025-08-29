package com.ec2.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ec2.main.model.Batches;
import com.ec2.main.model.Programs;

@Repository
public interface BatchesRepository extends JpaRepository<Batches, Long> {
    
    @Query(value = "SELECT * FROM ec2.BATCHES btc WHERE btc.BCHROWSTATE > 0 AND btc.BCHID = :batchId", nativeQuery = true)
    Batches getbtchId(@Param("batchId") Long batchId);

    List<Batches> findByPrograms(Programs program);

    @Query("SELECT b FROM Batches b JOIN Programs p ON b.programs.prgid = p.prgid " +
           "WHERE b.bchrowstate > 0 AND p.prgrowstate > 0 ORDER BY p.prgfield1 ASC, b.bchfield1 ASC")
    List<Batches> findAllActiveBatchesOrderedByProgramAndBatchField();

    List<Batches> findByPrograms_PrgidAndBchrowstateGreaterThanOrderByBchfield1Asc(Long prgId, Long rowState);



}
