package com.ec2.main.repository;

import com.ec2.main.model.StudentRegistrationCourses;

import jakarta.transaction.Transactional;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRegistrationCoursesRepository extends JpaRepository<StudentRegistrationCourses, Long> {
    
    @Query(value = "SELECT * FROM ec2.STUDENTREGISTRATIONCOURSES src WHERE src.SRCSRGID = :srgid AND src.SRCSTATUS='ACTIVE' AND src.SRCROWSTATE > 0", nativeQuery = true)
    List<StudentRegistrationCourses> getbysrgid(@Param("srgid") Long srgid);

    @Query("SELECT MAX(s.srcid) FROM StudentRegistrationCourses s")
    Optional<Long> findMaxSrcid();

    @Query(value = "SELECT * FROM ec2.STUDENTREGISTRATIONCOURSES src WHERE src.SRCSRGID = :srgid AND src.SRCTCRID = :tcrid AND src.SRCROWSTATE > 0", nativeQuery = true)
    StudentRegistrationCourses getSrg(@Param("tcrid") Long tcrid, @Param("srgid") Long srgid);

    @Modifying
    @Transactional
    @Query("DELETE FROM StudentRegistrationCourses s WHERE s.srcsrgid = :srgid")
    void deleteBySrcsrgid(@Param("srgid") Long srgid);

    @Modifying
    @Transactional
    @Query("DELETE FROM StudentRegistrationCourses s WHERE s.srcid = :srcid")
    void deleteSrcid(@Param("srcid") Long srcid);

    @Query(value = "SELECT * FROM ec2.STUDENTREGISTRATIONCOURSES src WHERE src.SRCSRGID = :srgid AND src.SRCROWSTATE > 0", nativeQuery = true)
    List<StudentRegistrationCourses> findBySrgid(Long srgid);

    @Query(value = "SELECT COUNT(*) FROM ec2.STUDENTREGISTRATIONCOURSES src WHERE src.SRCSRGID = :srgid AND src.SRCTCRID = :tcrid AND src.SRCSTATUS = 'ACTIVE' AND src.SRCROWSTATE > 0", nativeQuery = true)
    Long countActiveRegistrations(@Param("srgid") Long srgid, @Param("tcrid") Long tcrid);

    @Query(value = """
        SELECT tc.tcrid, c.crsname, c.crscode, src.srctype, tcc.tcccreditpoints, 
               tcc.tcclectures, tcc.tcctutorials, tcc.tccpracticals
        FROM ec2.studentregistrationcourses src
        JOIN ec2.termcourses tc ON src.srctcrid = tc.tcrid
        JOIN ec2.courses c ON tc.tcrcrsid = c.crsid
        JOIN ec2.termcoursecredits tcc ON tcc.tcctcrid = tc.tcrid
        WHERE src.srcsrgid = :registrationId
        ORDER BY c.crsname
        """, nativeQuery = true)
    List<Object[]> findActiveRegistrationCourseDetails(@Param("registrationId") Long registrationId);

    @Query(value = """
        SELECT c.crsname, c.crscode, COUNT(src.srctcrid), src.srctcrid
        FROM ec2.studentregistrationcourses src
        JOIN ec2.termcourses tcr ON tcr.tcrid = src.srctcrid
        JOIN ec2.courses c ON tcr.tcrcrsid = c.crsid
        WHERE tcr.tcrtrmid = :termId
        AND src.srcstatus = 'ACTIVE' AND src.srcrowstate > 0
        GROUP BY src.srctcrid, c.crsname, c.crscode
        ORDER BY c.crsname
    """,nativeQuery = true)
    List<Object[]> findRegisteredCoursesForTerm(@Param("termId") Long termId);

    @Query(value = """
        SELECT s.stdid, s.stdfirstname, s.stdlastname, s.stdinstid
        FROM ec2.studentregistrationcourses src
        JOIN ec2.studentregistrations srg ON src.srcsrgid = srg.srgid
        JOIN ec2.students s ON s.stdid = srg.srgstdid
        WHERE src.srctcrid = :termCourseId
        AND src.srcstatus = 'ACTIVE' AND src.srcrowstate > 0
        ORDER BY s.stdlastname, s.stdfirstname
        """, nativeQuery = true)
    List<Object[]> findActiveStudentsByTermCourseId(@Param("termCourseId") Long termCourseId);

}
