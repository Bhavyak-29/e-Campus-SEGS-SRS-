package com.segs.demo.service.impl;

import com.segs.demo.model.*;
import com.segs.demo.repository.*;
import com.segs.demo.service.facultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class facultyServiceImpl implements facultyService {

    @Autowired
    private TermRepository termRepository;

    @Autowired
    private ExamTypeRepository examTypeRepository;

    @Autowired
    private AcademicYearRepository academicYearRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<Term> getAllTerms() {
        return termRepository.findAll();
    }

    @Override
    public List<ExamType> getAllExamTypes() {
        return examTypeRepository.findAll();
    }

    @Override
    public List<AcademicYear> getAllAcademicYears() {
        return academicYearRepository.findAll();
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
}
