package com.ec2.main.repository;

import com.ec2.main.model.Terms;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TermsRepository extends JpaRepository<Terms, Long> {

    @Query(value = "SELECT * FROM ec2.terms trm, ec2.academicyears acdy " +
                   "WHERE trm.trmrowstate > 0 AND acdy.ayrrowstate > 0 " +
                   "AND trm.trmayrid = acdy.ayrid " +
                   "AND trm.trmid = :termId", nativeQuery = true)
    Terms gettrmId(@Param("termId") Long termId);

    @Query(value = "SELECT trm.trmid FROM ec2.terms trm, ec2.academicyears acdy " +
                   "WHERE trm.trmrowstate > 0 AND acdy.ayrrowstate > 0 " +
                   "AND trm.trmayrid = acdy.ayrid " +
                   "AND trm.trmname = :name AND acdy.ayrid = :ayrid", nativeQuery = true)
    Long findTermIdByName(@Param("name") String name, @Param("ayrid") Long ayrid);

    List<Terms> findByAcademicYear_Ayrid(Long academicYearId);

    @Query("SELECT DISTINCT t FROM Terms t WHERE t.trmrowstate > 0 ORDER BY t.trmname")
    List<Terms> findDistinctByTrmrowstateGreaterThan(int rowState);

    List<Terms> findByAcademicYear_AyridAndTrmrowstateGreaterThanOrderByTrmnameAsc(Long ayrId, int rowState);
}
