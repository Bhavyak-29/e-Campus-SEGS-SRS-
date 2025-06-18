package com.segs.demo.repository;

import com.segs.demo.model.Batch;
import com.segs.demo.model.Program;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BatchRepository extends JpaRepository<Batch, Long> { 
    List<Batch> findByProgram(Program program);

    @Query("SELECT b FROM Batch b JOIN Program p ON b.program.prgid = p.prgid " +
           "WHERE b.bchrowstate > 0 AND p.prgrowstate > 0 ORDER BY p.prgfield1 ASC, b.bchfield1 ASC")
    List<Batch> findAllActiveBatchesOrderedByProgramAndBatchField();

    List<Batch> findByProgram_PrgidAndBchrowstateGreaterThanOrderByBchfield1Asc(Long prgId, Short rowState);

}
