package com.segs.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.segs.demo.model.Course;
import com.segs.demo.model.Enrollment;
import com.segs.demo.model.Term;
import com.segs.demo.model.Users;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {
    Optional<Enrollment> findByStudentAndCourseAndTerm(Users student, Course course, Term term);
} 
