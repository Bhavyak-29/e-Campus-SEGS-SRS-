package com.segs.demo.service;

import com.segs.demo.model.Grade;
import com.segs.demo.model.StudentGradeDTO;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import com.segs.demo.model.GradeUploadForm;
public interface GradeService {
    List<Grade> getAllGrades();
    List<StudentGradeDTO> getStudentGrades(Long CRSID, Long examTypeId, List<String> selectedGrades);
    String uploadGrades(GradeUploadForm form, HttpSession session);

}
