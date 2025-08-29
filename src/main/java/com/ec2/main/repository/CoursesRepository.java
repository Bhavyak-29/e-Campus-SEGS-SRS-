package com.ec2.main.repository;

import com.ec2.main.model.Courses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface CoursesRepository extends JpaRepository<Courses, Long> {
    
    @Query(value = "SELECT * FROM ec2.COURSES crs WHERE crs.CRSID = :crsid AND crs.CRSROWSTATE > 0", nativeQuery = true)
    Courses getbycrsid(@Param("crsid") Long crsid);

    @Query(value = "SELECT * FROM ec2.COURSEGROUPCOURSES cgc, ec2.COURSES crs WHERE cgc.CGCROWSTATE > 0 AND crs.CRSROWSTATE > 0 AND crs.CRSID = cgc.CGCCRSID AND cgc.CGCCGPID = :cgpId AND crs.CRSID IN (SELECT tc.tcrcrsid FROM ec2.TERMCOURSEAVAILABLEFOR tca, ec2.TERMCOURSES tc WHERE tca.tcaprgid = :prgId AND tca.tcatcrid = tc.tcrid AND tcastatus = 'T' AND tca.tcarowstate > 0 AND tc.tcrrowstate > 0 AND tc.tcrtrmid = :trmId) ORDER BY crs.crsname", nativeQuery = true)
    List<Courses> getbycgpId(@Param("cgpId") Long cgpId, @Param("prgId") Long prgId, @Param("trmId") Long trmId);

    @Query(value = "SELECT crs.CRSID FROM ec2.COURSES crs WHERE crs.CRSROWSTATE > 0 AND crs.CRSCODE = :courseCode", nativeQuery = true)
    Long findCourseIdByName(@Param("courseCode") String courseCode);

    List<Courses> findByTerm_Trmid(Long trmId);

}
