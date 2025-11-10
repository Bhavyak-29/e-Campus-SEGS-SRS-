package com.ec2.main.repository;

import com.ec2.main.model.TermCourseAvailableFor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TermCourseAvailableForRepository extends JpaRepository<TermCourseAvailableFor, Long> {
    // Custom queries can be added here
    @Query("SELECT t FROM TermCourseAvailableFor t WHERE t.tcaprgid = :prgid AND t.tcabchid = :bchid " +
           "AND t.tcatcrid IN (SELECT c.tcrid FROM TermCourses c WHERE c.tcrtrmid = :termId)")
    List<TermCourseAvailableFor> findByTermProgramBatch(Long termId, Long prgid, Long bchid);

    @Query("SELECT COALESCE(MAX(t.tcaid), 0) FROM TermCourseAvailableFor t")
    Long getMaxTcaId();
}
