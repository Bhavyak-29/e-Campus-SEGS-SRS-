package com.ec2.main.repository;

import com.ec2.main.model.StudentAdjustmentCourses;

import jakarta.transaction.Transactional;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAdjustmentCoursesRepository extends JpaRepository<StudentAdjustmentCourses, Long> {
    
    @Query(value = "SELECT * FROM ec2.STUDENTADJUSTMENTCOURSES sac WHERE sac.SACSRGID = :srgid AND sac.SACTCRID = :tcrid AND sac.SACROWSTATE>0", nativeQuery = true)
    StudentAdjustmentCourses findSacid(@Param("tcrid") Integer tcrid, @Param("srgid") Long srgid);

    @Query(value = "SELECT MAX(sac.SACID) FROM ec2.STUDENTADJUSTMENTCOURSES sac", nativeQuery = true)
    Optional<Long> getMaxId();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ec2.STUDENTADJUSTMENTCOURSES sac WHERE sac.SACID = :sacid", nativeQuery = true)
    void deleteSacid(@Param("scrid") Long sacid);

    @Query(value = "SELECT * FROM ec2.STUDENTADJUSTMENTCOURSES sac WHERE sac.SACSRGID = :srgid AND sac.SACROWSTATE>0", nativeQuery = true)
    List<StudentAdjustmentCourses> findBySrgid(Long srgid);
}
