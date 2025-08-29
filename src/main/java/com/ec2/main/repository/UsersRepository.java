package com.ec2.main.repository;

import com.ec2.main.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    // You can add custom query methods here if needed
}

