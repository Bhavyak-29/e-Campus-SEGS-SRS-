package com.segs.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.segs.demo.model.User2;

@Repository
public interface User2Repository  extends JpaRepository<User2,Long>{

    
}
