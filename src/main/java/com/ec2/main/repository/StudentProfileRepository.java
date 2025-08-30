package com.ec2.main.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ec2.main.model.StudentProfile;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    StudentProfile findByStdid(Long stdid);
}
