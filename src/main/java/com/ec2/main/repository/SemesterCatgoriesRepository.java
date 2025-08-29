package com.ec2.main.repository;

import com.ec2.main.model.SemesterCatgories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterCatgoriesRepository extends JpaRepository<SemesterCatgories, Long> {
    // Custom queries can be added here
}
