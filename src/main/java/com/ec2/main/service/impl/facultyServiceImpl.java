package com.ec2.main.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ec2.main.model.AcademicYears;
import com.ec2.main.model.Courses;
import com.ec2.main.model.Egcrstt1;
import com.ec2.main.model.Terms;
import com.ec2.main.model.Users;
import com.ec2.main.repository.AcademicYearsRepository;
import com.ec2.main.repository.CoursesRepository;
import com.ec2.main.repository.Egcrstt1Repository;
import com.ec2.main.repository.TermsRepository;
import com.ec2.main.repository.UserRepository;
import com.ec2.main.service.facultyService;

@Service
public class facultyServiceImpl implements facultyService {

    @Autowired
    private TermsRepository termsRepository;

    @Autowired
    private Egcrstt1Repository examTypeRepository;

    @Autowired
    private AcademicYearsRepository academicYearsRepository;

    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Terms> getAllTerms() {
        return termsRepository.findAll();
    }

    @Override
    public List<Egcrstt1> getAllExamTypes() {
        return examTypeRepository.findAll();
    }

    @Override
    public List<AcademicYears> getAllAcademicYears() {
        return academicYearsRepository.findAll();
    }

    @Override
    public List<Courses> getAllCourses() {
        return coursesRepository.findAll();
    }

    @Override
public List<Users> getAllFaculties() {
    return userRepository.findAllFacultyList(); // see repo method below
}

@Override
public List<Users> searchFaculties(String keyword) {
    return userRepository.searchFacultyList(keyword);
}

}
