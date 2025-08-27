package com.ec2.main.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ec2.main.model.AcademicYear;
import com.ec2.main.model.Course;
import com.ec2.main.model.Egcrstt1;
import com.ec2.main.model.Term;
import com.ec2.main.repository.AcademicYearRepository;
import com.ec2.main.repository.CourseRepository;
import com.ec2.main.repository.Egcrstt1Repository;
import com.ec2.main.repository.TermRepository;
import com.ec2.main.service.facultyService;

@Service
public class facultyServiceImpl implements facultyService {

    @Autowired
    private TermRepository termRepository;

    @Autowired
    private Egcrstt1Repository examTypeRepository;

    @Autowired
    private AcademicYearRepository academicYearRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<Term> getAllTerms() {
        return termRepository.findAll();
    }

    @Override
    public List<Egcrstt1> getAllExamTypes() {
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
