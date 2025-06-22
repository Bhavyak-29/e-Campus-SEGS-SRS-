// src/main/java/com/segs/demo/repository/TermCourseRepository.java
package com.segs.demo.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.segs.demo.model.DropdownItem;
import com.segs.demo.model.TermCourse;

public interface TermCourseRepository extends JpaRepository<TermCourse, Long> {

    List<TermCourse> findByTerm_Id(Long termId);

    @Query("SELECT t.id FROM TermCourse t WHERE t.course.id = :crsid AND t.term.id = :trmid")
    Long findTcridByCrsidAndTrmid(@Param("crsid") Long crsid, @Param("trmid") Long trmid);

    List<TermCourse> findByTerm_IdAndUser_UserId(Long trmId, Long userId);

    List<TermCourse> findByTerm_AcademicYear_IdAndTerm_Id(Long academicYearId, Long termId);

     @Query("SELECT NEW com.segs.demo.model.DropdownItem(CAST(tc.id AS string), CONCAT(c.name, '-(', c.code, ')')) " +
           "FROM TermCourse tc JOIN Course c ON tc.course.id = c.id " +
           "WHERE (tc.rowState > 0 OR tc.rowState IS NULL) " +
           "AND (c.rowState > 0 OR c.rowState IS NULL) " +
           "AND tc.term.id = :termId " +
           "AND EXISTS (SELECT 1 FROM Egcrstt1 e WHERE e.id.tcrid = tc.id AND e.rowStatus = '1') " + // Assuming '1' means submitted
           "ORDER BY c.name")
    List<DropdownItem> findSubmittedTermCoursesByTermId(@Param("termId") Long termId);

    /**
     * Retrieves a list of DropdownItem for TermCourses that have at least one
     * associated Egcrstt1 entry with grades that have been 'updated'
     * (i.e., updatedBy and updatedDate are not null).
     * Filters by active TermCourse and Course records.
     */
    @Query("SELECT NEW com.segs.demo.model.DropdownItem(CAST(tc.id AS string), CONCAT(c.name, '-(', c.code, ')')) " +
           "FROM TermCourse tc JOIN Course c ON tc.course.id = c.id " +
           "WHERE (tc.rowState > 0 OR tc.rowState IS NULL) " +
           "AND (c.rowState > 0 OR c.rowState IS NULL) " +
           "AND tc.term.id = :termId " +
           "AND EXISTS (SELECT 1 FROM Egcrstt1 e WHERE e.id.tcrid = tc.id AND e.updatedBy IS NOT NULL AND e.updatedDate IS NOT NULL) " + // Key condition for "updated"
           "ORDER BY c.name")
    List<DropdownItem> findUpdatedTermCoursesByTermId(@Param("termId") Long termId);

    // This method is for showUpdatedGradeListReport to get TermCourse details
    List<TermCourse> findByTerm_IdAndRowStateGreaterThan(Long termId, int rowstate);
    
    // Method to get a TermCourse by its ID, for populating display names in the report header
    // No need to define this if JpaRepository's findById is sufficient and you are importing TermCourse
}
