package com.ec2.main.repository;

import com.ec2.main.model.EGEXAMM1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EGEXAMM1Repository extends JpaRepository<EGEXAMM1, Long> {
    // Custom queries can be added here
}
