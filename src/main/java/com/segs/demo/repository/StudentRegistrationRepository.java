package com.segs.demo.repository;

import com.segs.demo.model.StudentRegistration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StudentRegistrationRepository extends JpaRepository<StudentRegistration, Long> {

    // --- Existing methods (unchanged) ---
    List<StudentRegistration> findBysrgstdidAndSrgrowstateGreaterThanOrderBySrgidDesc(Long studentId, int state);
    Optional<StudentRegistration> findTopBysrgstdidOrderBySrgidDesc(Long studentId);
   
}