
package com.ec2.main.repository;

import java.util.List;

import com.ec2.main.model.Users;

import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<Users, Integer> {
    List<Users> findByUserCategory(String userCategory);
}