package com.segs.demo.service;

import com.segs.demo.model.*;

import java.util.List;

public interface facultyService {
    List<Term> getAllTerms();
    List<ExamType> getAllExamTypes();
    List<AcademicYear> getAllAcademicYears();
    List<Course> getAllCourses();
}
