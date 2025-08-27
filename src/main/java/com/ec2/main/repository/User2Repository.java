package com.ec2.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ec2.main.model.User2;

@Repository
public interface User2Repository  extends JpaRepository<User2,Long>{

    
}
