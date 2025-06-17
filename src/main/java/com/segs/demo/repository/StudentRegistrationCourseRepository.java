package com.segs.demo.repository;

import com.segs.demo.model.StudentRegistrationCourse;
// import com.segs.demo.dto.StudentCourseDetailsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRegistrationCourseRepository extends JpaRepository<StudentRegistrationCourse, Long> {

    /**
     * Corresponds to the complex course details query from StudentSearch.jsp.
     * It is highly recommended to create a DTO class (e.g., StudentCourseDetailsDto)
     * to hold the results of this native query for type safety.
     */
    @Query(value = "SELECT tc.tcrid, c.crsname, c.crscode, src.srctype, tcc.tcccreditpoints, tcc.tcclectures, tcc.tcctutorials, tcc.tccpracticals " +
            "FROM ec2.studentregistrationcourses src " +
            "JOIN ec2.termcourses tc ON src.srctcrid = tc.tcrid " +
            "JOIN ec2.courses c ON tc.tcrcrsid = c.crsid " +
            "JOIN ec2.termcoursecredits tcc ON tcc.tcctcrid = tc.tcrid " +
            "WHERE src.srcsrgid = :registrationId " +
            //"AND src.srcstatus = 'ACTIVE' AND src.srcrowstate > 0 AND tc.tcrrowstate > 0 AND c.crsrowstate > 0 AND tcc.tccrowstate > 0 " +
            //"AND (SELECT count(*) FROM ec2.work_trail wt WHERE wt.work_id = :registrationId AND wt.work_type_code = 21 AND wt.node_number = 3 AND wt.response_code = 1) > 0 " +
            "ORDER BY c.crsname", nativeQuery = true)
    List<Object[]> findActiveRegistrationCourseDetails(@Param("registrationId") Long registrationId);

}