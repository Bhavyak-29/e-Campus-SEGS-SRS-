package com.ec2.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ec2.main.model.DropdownItem;
import com.ec2.main.model.ExamType;

@Repository
public interface ExamTypeRepository extends JpaRepository<ExamType, Long> {
    List<ExamType> findByCourse_Id(Long crsId);
    List<ExamType> findByIdIn(List<Long> examtypeIds);

    // This method is for ResultController to get ExamType details for display in report header
    // Assuming ExamType is an entity and it has 'id' and 'examtypeName' properties.
    Optional<ExamType> findExamTypeById(Long id);

    // ExamTypeRepository.java
    @Query("SELECT NEW com.ec2.main.model.DropdownItem(CAST(et.id AS string), et.title) " +
           "FROM ExamType et WHERE et.rowState > 0 " +
           "AND EXISTS (SELECT 1 FROM Egcrstt1 e WHERE e.id.examtypeId = et.id AND e.id.tcrid = :termCourseId AND e.rowStatus = '1')")
    List<DropdownItem> findExamTypesWithGradesByTermCourseId(@Param("termCourseId") Long termCourseId);

}
