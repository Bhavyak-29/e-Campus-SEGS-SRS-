package com.ec2.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ec2.main.model.AcademicYears;

@Repository
public interface AcademicYearsRepository extends JpaRepository<AcademicYears, Long> {

    @Query(value = "SELECT ay.AYRID FROM ec2.ACADEMICYEARS ay WHERE ay.AYRNAME = :name AND ay.AYRROWSTATE > 0", nativeQuery = true)
    Long findAcademicYearIdByName(@Param("name") String name);

    List<AcademicYears> findByAyrrowstateGreaterThan(int rowState);

    List<AcademicYears> findByAyrrowstateGreaterThanOrderByAyrfield1Asc(int rowState);
}
