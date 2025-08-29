package com.ec2.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ec2.main.model.Semesters;
import com.ec2.main.model.StudentRegistrations;
import com.ec2.main.model.Students;
import com.ec2.main.repository.SemestersRepository;
import com.ec2.main.repository.StudentRegistrationsRepository;
import com.ec2.main.repository.StudentsRepository;

@Service
public class StudentRegistrationService {
    @Autowired 
    private StudentsRepository studentRepo;
    
    @Autowired
    private SemestersRepository semesterRepo;
    
    @Autowired 
    private StudentRegistrationsRepository registrationRepo;
    

    public Students getStudentById(Long id) {
        return studentRepo.findStudent(id);
    }

    public List<Semesters> getSemesterById(Long id) {
        return semesterRepo.findActiveSemestersByBranchId(id);
    }

    public Long getMaxSemesterId(Long batchId) {
        return semesterRepo.findMaxSemesterId(batchId);
    }
    public List<StudentRegistrations> getRegistrationsByStudentId(Long studentId) {
         return registrationRepo.findregisteredsemesters(studentId);
    }

}
