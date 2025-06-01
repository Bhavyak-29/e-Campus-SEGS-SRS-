
package com.segs.demo.repository;

import java.util.List;

import com.segs.demo.model.Users;

import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<Users, Integer> {
    List<Users> findByUserCategory(String userCategory);
}