package com.segs.demo.repository;

import com.segs.demo.model.Batch;
import com.segs.demo.model.Program;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BatchRepository extends JpaRepository<Batch, Long> { 
    List<Batch> findByProgram(Program program);
}
