package com.segs.demo.repository;

import com.segs.demo.model.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {
    
    // Custom derived query methods (optional)
    AcademicYear findByName(String name);

    // Add more if needed:
    // List<AcademicYear> findByRowState(Integer rowState);
    // List<AcademicYear> findByField1GreaterThan(Integer value);
}
