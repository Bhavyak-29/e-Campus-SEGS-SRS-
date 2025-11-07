package com.ec2.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ec2.main.model.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    List<Users> findByUrole0(String urole0);

    Optional<Users> findByUnivId(String univId);

    @Query(value = "SELECT u.* FROM ec2.users u JOIN ec2.ec2_roles r ON u.urole = r.rid WHERE u.univId = :univid", nativeQuery = true)
    Optional<Users> findByUnividWithRoles(@Param("univid") String univId);

    // 🔹 All faculty (urole = '913', row_state > 0) without pagination
    @Query(value = """
        SELECT * FROM ec2.users
        WHERE urole = '913'
          AND row_state > 0
        ORDER BY ufullname
        """,
        nativeQuery = true)
    List<Users> findAllFacultyList();

    // 🔹 Full-text search among faculty (urole = '913', row_state > 0) without pagination
    @Query(value = """
        SELECT * FROM ec2.users
        WHERE urole = '913'
          AND row_state > 0
          AND to_tsvector('english',
                coalesce(uname, '') || ' ' ||
                coalesce(ufullname, '') || ' ' ||
                coalesce(uemail, '')
              ) @@ plainto_tsquery('english', :keyword)
        ORDER BY ufullname
        """,
        nativeQuery = true)
    List<Users> searchFacultyList(@Param("keyword") String keyword);
}
