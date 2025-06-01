package com.segs.demo.repository;

import com.segs.demo.model.Batch;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BatchRepository extends JpaRepository<Batch, Long> { }
