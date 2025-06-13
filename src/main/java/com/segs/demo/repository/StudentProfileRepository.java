package com.segs.demo.repository;
import com.segs.demo.model.StudentProfile;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    StudentProfile findBystdid(Long stdid);
}
