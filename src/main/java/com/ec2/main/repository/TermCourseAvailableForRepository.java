package com.ec2.main.repository;

import com.ec2.main.model.TermCourseAvailableFor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermCourseAvailableForRepository extends JpaRepository<TermCourseAvailableFor, Integer> {
    // Custom queries can be added here
}
