package com.segs.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.segs.demo.model.AcademicYear;
import com.segs.demo.model.Course;
import com.segs.demo.model.ExamType;
import com.segs.demo.model.Term;
import com.segs.demo.repository.AcademicYearRepository;
import com.segs.demo.repository.CourseRepository;
import com.segs.demo.repository.ExamTypeRepository;
import com.segs.demo.repository.TermRepository;
import com.segs.demo.service.facultyService;

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
