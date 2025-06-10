package com.segs.demo.service;

import java.util.List;

import com.segs.demo.model.Grade;
import com.segs.demo.model.GradeUploadForm;
import com.segs.demo.model.StudentGradeDTO;

import jakarta.servlet.http.HttpSession;

public interface GradeService {
    List<Grade> getAllGrades();
    // Updated method signature to include trmid
    List<StudentGradeDTO> getStudentGrades(Long CRSID, Long trmid, Long examTypeId, List<String> selectedGrades);
    String uploadGrades(GradeUploadForm form, HttpSession session);
}
