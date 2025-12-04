package com.ec2.main.repository;

import com.ec2.main.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    // You can add custom query methods here if needed
    @Query(value = """
    SELECT * 
    FROM ec2.users 
    WHERE urole_0 = 'FACULTY' 
      AND LOWER(uemail) LIKE LOWER(CONCAT('%', :query, '%'))
""", nativeQuery = true)
    List<Users> searchFacultyByName(@Param("query") String query);

    List<Users> findByUemailContainingIgnoreCase(String name);
    Optional<Users> findFirstByUnivIdAndRowStateGreaterThan(String univId, short rowState);
}


