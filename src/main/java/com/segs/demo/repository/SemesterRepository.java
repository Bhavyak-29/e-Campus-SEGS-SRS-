package com.segs.demo.repository;

import com.segs.demo.model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

}