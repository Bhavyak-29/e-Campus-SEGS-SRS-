package com.ec2.main.repository;
import com.ec2.main.model.StudentProfile;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    StudentProfile findBystdid(Long stdid);
}
