package com.segs.demo.repository;

import com.segs.demo.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByStdinstidAndStdrowstateGreaterThan(Long instId, int state);
    List<Student> findByStdfirstnameContainingIgnoreCaseAndStdlastnameContainingIgnoreCaseAndStdrowstateGreaterThan(String fname, String lname, int state);
    List<Student> findByStdfirstnameContainingIgnoreCaseAndStdrowstateGreaterThan(String fname, int state);
    List<Student> findByStdlastnameContainingIgnoreCaseAndStdrowstateGreaterThan(String lname, int state);
}

