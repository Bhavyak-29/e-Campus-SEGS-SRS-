package com.segs.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.segs.demo.model.ExamType;

@Repository
public interface ExamTypeRepository extends JpaRepository<ExamType, Long> {
    List<ExamType> findByCourse_Id(Long crsId);
    List<ExamType> findByIdIn(List<Long> examtypeIds);

}