package com.ec2.main.repository;

import com.ec2.main.model.CourseGroups;
import com.ec2.main.model.SemesterCourses;

import org.antlr.v4.runtime.atn.SemanticContext.AND;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;
import org.springframework.data.repository.query.Param;

@Repository
public interface SemesterCoursesRepository extends JpaRepository<SemesterCourses, Long> {
    // Custom queries can be added here
    @Query(value = "SELECT * FROM ec2.SEMESTERCOURSES sc, ec2.COURSES c, ec2.SEMESTERS s WHERE sc.SCRROWSTATE > 0 AND s.STRROWSTATE > 0 AND c.CRSROWSTATE > 0 AND c.CRSID = sc.SCRCRSID AND s.STRID = sc.SCRSTRID AND sc.SCRELECTIVE = 'N' AND sc.SCRSTRID = :semesterId", nativeQuery = true)
    List<SemesterCourses> getccbysemid(@Param("semesterId") Long semesterId);

    @Query(value = "SELECT * FROM ec2.SEMESTERCOURSES sc, ec2.COURSES c, ec2.SEMESTERS s WHERE sc.SCRROWSTATE > 0 AND s.STRROWSTATE > 0 AND c.CRSROWSTATE > 0 AND c.CRSID = sc.SCRCRSID AND s.STRID = sc.SCRSTRID AND sc.SCRELECTIVE = 'Y' AND s.STRBCHID = :batchId AND s.STRID < :semesterId AND sc.SCRID NOT IN (SELECT SRC.SRCSCRID FROM ec2.STUDENTREGISTRATIONS SRG, ec2.STUDENTREGISTRATIONCOURSES SRC, ec2.STUDENTSCORES SSC WHERE SRC.SRCROWSTATE > 0 AND SRG.SRGROWSTATE > 0 AND SSC.SSCROWSTATE > 0 AND SRC.SRCSRGID = SRG.SRGID AND SRC.SRCTCRID = SSC.SSCTCRID AND SSC.SSCSTDID = SRG.SRGSTDID AND SRG.SRGSTDID = :studentId)", nativeQuery = true)
    List<SemesterCourses> getBCCourses(@Param("studentId") Long studentId, @Param("semesterId") Long semesterId, @Param("batchId") Long batchId);

    @Query(value = "SELECT * FROM ec2.SEMESTERCOURSES sc, ec2.COURSEGROUPS cg, ec2.SEMESTERS s WHERE sc.SCRROWSTATE > 0 AND s.STRROWSTATE > 0 AND cg.CGPROWSTATE > 0 AND cg.CGPID = sc.SCRCGPID AND s.STRID = sc.SCRSTRID AND sc.SCRELECTIVE = 'Y' AND s.STRBCHID = :batchId AND s.STRID < :semesterId AND sc.SCRID NOT IN (SELECT SRC.SRCSCRID FROM ec2.STUDENTREGISTRATIONS SRG, ec2.STUDENTREGISTRATIONCOURSES SRC, ec2.STUDENTSCORES SSC WHERE SRC.SRCROWSTATE > 0 AND SRG.SRGROWSTATE > 0 AND SSC.SSCROWSTATE > 0 AND SRC.SRCSRGID = SRG.SRGID AND SRC.SRCTCRID = SSC.SSCTCRID AND SSC.SSCSTDID = SRG.SRGSTDID AND SRG.SRGSTDID = :studentId)", nativeQuery = true)
    List<SemesterCourses> getBECourses(@Param("studentId") Long studentId, @Param("semesterId") Long semesterId, @Param("batchId") Long batchId);

    @Query(value = "SELECT sc.SCRID FROM ec2.SEMESTERCOURSES sc WHERE sc.SCRSTRID = :semesterId AND sc.SCRCRSID = :courseId", nativeQuery = true)
    Long findScrid(@Param("semesterId") Long semesterId, @Param("courseId") Long courseId);

}
