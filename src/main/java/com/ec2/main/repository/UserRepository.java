package com.ec2.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ec2.main.model.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    
    // fetch all users by urole0 value
    List<Users> findByUrole0(String urole0);
}
