// src/main/java/com/segs/demo/repository/TermCourseCreditsRepository.java
package com.ec2.main.repository;

import java.math.BigDecimal;
import java.util.Optional; // Import Optional

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ec2.main.model.TermCourseCredits;

@Repository
public interface TermCourseCreditsRepository extends JpaRepository<TermCourseCredits, Long> {

    /**
     * Finds the credit points (tcccreditpoints) for a given term course ID (tcctcrid).
     * Returns an Optional to gracefully handle cases where:
     * - No matching record is found (returns Optional.empty()).
     * - (Less common for this query if tcctcrid is truly unique) Multiple records are found,
     * though Spring Data JPA would typically throw IncorrectResultSizeDataAccessException
     * in such a scenario if it were directly mapping to a single entity/value.
     * Using Optional allows for clearer intent and handling of absence.
     *
     * @param tcctcrid The ID of the term course.
     * @return An Optional containing the BigDecimal credit points if found,
     * or an empty Optional if no matching record is found.
     */
    @Query("SELECT t.tcccreditpoints FROM TermCourseCredits t WHERE t.tcctcrid = :tcctcrid")
    Optional<BigDecimal> findTcccreditpointsByTcctcrid(@Param("tcctcrid") Long tcctcrid);
    Optional<TermCourseCredits> findByTcctcrid(Long tcctcrid);
}
