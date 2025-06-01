package com.segs.demo.repository;

import com.segs.demo.model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SemesterRepository extends JpaRepository<Semester, Long> { }
