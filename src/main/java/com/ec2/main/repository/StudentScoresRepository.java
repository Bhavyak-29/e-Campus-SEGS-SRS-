package com.ec2.main.repository;

import com.ec2.main.model.StudentScores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentScoresRepository extends JpaRepository<StudentScores, Long> {
    // Custom queries can be added here
}
