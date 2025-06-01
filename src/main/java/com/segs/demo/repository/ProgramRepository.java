package com.segs.demo.repository;

import com.segs.demo.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProgramRepository extends JpaRepository<Program, Long> { }
