package com.ec2.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ec2.main.model.Courses;
import com.ec2.main.model.Enrollment;
import com.ec2.main.model.Terms;
import com.ec2.main.model.Users;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {
    Optional<Enrollment> findByStudentAndCourseAndTerm(Users student, Courses course, Terms term);
} 
