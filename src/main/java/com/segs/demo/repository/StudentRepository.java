package com.segs.demo.repository;

import com.segs.demo.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByRollNo(String rollNo);
    List<Student> findByBranchAndBatchAndSection(String branch, String batch, String section);
}

