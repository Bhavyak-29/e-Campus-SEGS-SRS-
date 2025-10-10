package com.ec2.main.service;

import java.util.List;
import java.util.Map;

import com.ec2.main.model.DropdownItem;
import com.ec2.main.model.Grade;
import com.ec2.main.model.GradeChangeStatusDTO;
import com.ec2.main.model.GradeUploadForm;
import com.ec2.main.model.StudentGradeDTO;

import jakarta.servlet.http.HttpSession;

public interface GradeService {
    List<Grade> getAllGrades();

    Map<Long, Long> getGradeDistribution();

    List<StudentGradeDTO> getStudentGrades(Long CRSID, Long trmid, Long examTypeId, List<String> selectedGrades);

    List<StudentGradeDTO> getUpdatedStudentGrades(Long CRSID, Long trmid, Long examTypeId, List<String> selectedGrades);

    List<DropdownItem> getUpdatedTermCoursesByTermId(Long termId);

    List<DropdownItem> getExamTypesWithUpdatedGradesByTermCourseId(Long termCourseId);

    List<StudentGradeDTO> getUpdatedStudentGradesForReport(Long termCourseId, Long examTypeId, List<String> selectedGrades);

    String uploadGrades(GradeUploadForm form, HttpSession session);

    void saveOrUpdateGrades(List<StudentGradeDTO> gradesList, Long tcrid, Long examTypeId);

    String getTermName(Long termId);

    String getCourseName(Long courseId);

    void submitGradeChange(Long facultyId, Long tcrid, Long examtypeId, String studId, String newGradeLetter, String remarks);

    List<GradeChangeStatusDTO> getGradeChangeStatuses(Long facultyId, String facultyUsername,Long tcrid);

    List<GradeChangeStatusDTO> getPendingDeanRequests();

    public List<GradeChangeStatusDTO> getPendingRegistrarRequests();

    boolean processDeanAction(Long gmdid, Long deanId, String action);

    boolean processRegistrarAction(Long gmdid, Long deanId, String action);
}