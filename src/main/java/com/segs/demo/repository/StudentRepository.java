package com.segs.demo.repository;

import com.segs.demo.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Page<Student> findByStdinstidAndStdrowstateGreaterThan(Long instId, int state, Pageable pageable);
    Page<Student> findByStdfirstnameContainingIgnoreCaseAndStdlastnameContainingIgnoreCaseAndStdrowstateGreaterThan(String fname, String lname, int state, Pageable pageable);
    Page<Student> findByStdfirstnameContainingIgnoreCaseAndStdrowstateGreaterThan(String fname, int state, Pageable pageable);
    Page<Student> findByStdlastnameContainingIgnoreCaseAndStdrowstateGreaterThan(String lname, int state, Pageable pageable);
}


