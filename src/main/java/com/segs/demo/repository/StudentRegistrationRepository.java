package com.segs.demo.repository;

import com.segs.demo.model.StudentRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRegistrationRepository extends JpaRepository<StudentRegistration, Long> {
    List<StudentRegistration> findBySrgstdidAndSrgrowstateGreaterThanOrderBySrgidDesc(Long studentId, int state);
    Optional<StudentRegistration> findTopBySrgstdidOrderBySrgidDesc(Long studentId);
}
