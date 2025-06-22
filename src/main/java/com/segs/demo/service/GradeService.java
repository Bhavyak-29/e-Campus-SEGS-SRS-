package com.segs.demo.service;

import java.util.List;
import java.util.Map;

import com.segs.demo.model.DropdownItem; // Import DropdownItem
import com.segs.demo.model.Grade;
import com.segs.demo.model.GradeUploadForm;
import com.segs.demo.model.StudentGradeDTO;

import jakarta.servlet.http.HttpSession;

public interface GradeService {
    List<Grade> getAllGrades();
    Map<Integer, Long> getGradeDistribution();
    // Map<String, Long> getGradeDistributionWithLetters();

    // Existing methods for direct grade entry (your friend's functionality)
    List<StudentGradeDTO> getStudentGrades(Long CRSID, Long trmid, Long examTypeId, List<String> selectedGrades);
    List<StudentGradeDTO> getUpdatedStudentGrades(Long CRSID, Long trmid, Long examTypeId, List<String> selectedGrades);

    // New methods to support the "results -> course-wise -> updated grade" selector
    // Retrieves TermCourses that have updated grades for a given Term ID
    List<DropdownItem> getUpdatedTermCoursesByTermId(Long termId);
    // Retrieves ExamTypes that have updated grades for a given TermCourse ID
    List<DropdownItem> getExamTypesWithUpdatedGradesByTermCourseId(Long termCourseId);

    // New method for fetching updated grades for the report, using termCourseId directly
    List<StudentGradeDTO> getUpdatedStudentGradesForReport(Long termCourseId, Long examTypeId, List<String> selectedGrades);

    String uploadGrades(GradeUploadForm form, HttpSession session);
    void saveOrUpdateGrades(List<StudentGradeDTO> gradesList, Long tcrid, Long examTypeId);
    String getTermName(Long termId);
    String getCourseName(Long courseId);
}