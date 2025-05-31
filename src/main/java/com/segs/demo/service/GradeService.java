package com.segs.demo.service;

import com.segs.demo.model.Grade;
import com.segs.demo.model.StudentGradeDTO;

import java.util.List;

public interface GradeService {
    List<Grade> getAllGrades();

    List<StudentGradeDTO> getStudentGrades(Long CRSID, Long examTypeId, List<String> selectedGrades);
}
