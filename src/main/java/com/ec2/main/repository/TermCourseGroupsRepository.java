package com.ec2.main.repository;

import com.ec2.main.model.TermCourseGroups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermCourseGroupsRepository extends JpaRepository<TermCourseGroups, Long> {
    // Custom queries can be added here
}
