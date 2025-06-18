package com.segs.demo.repository;

import com.segs.demo.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramRepository extends JpaRepository<Program, Long> { 
   List<Program> findByPrgrowstateGreaterThanOrderByPrgfield1Asc(short prgrowstate);
}
