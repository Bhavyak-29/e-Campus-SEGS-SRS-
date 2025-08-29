package com.ec2.main.repository;

import com.ec2.main.model.CourseGroupTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseGroupTypesRepository extends JpaRepository<CourseGroupTypes, Long> {
    // Custom queries can be added here
}
