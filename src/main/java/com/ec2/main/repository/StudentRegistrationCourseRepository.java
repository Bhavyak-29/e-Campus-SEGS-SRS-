package com.ec2.main.repository;

import com.ec2.main.model.StudentRegistrationCourse;
// import com.ec2.main.dto.StudentCourseDetailsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface StudentRegistrationCourseRepository extends JpaRepository<StudentRegistrationCourse, Long> {

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

