package com.ec2.main.repository;

import com.ec2.main.model.BatchSemesterCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchSemesterCategoriesRepository extends JpaRepository<BatchSemesterCategories, Long> {
    // Custom queries can be added here
}
