package com.ec2.main.repository;

import com.ec2.main.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramRepository extends JpaRepository<Program, Long> { 
   List<Program> findByPrgrowstateGreaterThanOrderByPrgfield1Asc(short prgrowstate);
}
