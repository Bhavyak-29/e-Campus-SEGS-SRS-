package com.ec2.main.service;

import com.ec2.main.model.*;

import java.util.List;

public interface facultyService {
    List<Term> getAllTerms();
    List<Egcrstt1> getAllExamTypes();
    List<AcademicYear> getAllAcademicYears();
    List<Course> getAllCourses();
}
