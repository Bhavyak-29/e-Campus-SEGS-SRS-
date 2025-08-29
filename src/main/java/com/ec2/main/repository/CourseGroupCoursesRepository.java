package com.ec2.main.repository;

import com.ec2.main.model.CourseGroupCourses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseGroupCoursesRepository extends JpaRepository<CourseGroupCourses, Long> {
    // Custom queries can be added here
}
