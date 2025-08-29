package com.ec2.main.service;

import com.ec2.main.model.*;
import com.ec2.main.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.LongAdder;

@Service
public class StudentRegistrationEditService {
    
    @Autowired
    private SemestersRepository semesterRepo;
    
    @Autowired 
    private StudentRegistrationsRepository registrationRepo;

    @Autowired
    private BatchesRepository batchesRepo;

    @Autowired
    private ProgramsRepository programsRepo;

    @Autowired
    private TermsRepository termsRepo;

    @Autowired
    private StudentRegistrationCoursesRepository strcRepo;

    @Autowired
    private TermCoursesRepository trmcrsRepo;

    @Autowired
    private CoursesRepository crsRepo;

    @Autowired
    private SemesterCoursesRepository semesterCoursesRepo;

    @Autowired
    private CourseGroupsRepository courseGroupsRepo;

    @Autowired
    private StudentSemesterResultRepository stdstrresultRepo;
    
    public Semesters getSemesterBystrId(Long id) {
        return semesterRepo.findById(id).orElse(null);
    }

    public StudentRegistrations getRegistrationsByStudentIdandSemesterId(Long studentId, Long semesterId) {
        return registrationRepo.getsrgbystdidandstrid(studentId, semesterId);
    }

    public Batches getbatch(Long id) {
        return batchesRepo.getbtchId(id);
    }

    public Programs getprogram(Long id) {
        return programsRepo.getprgId(id);
    }

    public Terms getTerm(Long id) {
        return termsRepo.gettrmId(id);
    }

    public List<StudentRegistrationCourses> getStudentRegistrationCourses(Long id) {
        return strcRepo.getbysrgid(id);
    }

    public TermCourses getTermCourse(Long termcourseId) {
        return trmcrsRepo.getbytrmcrsid(termcourseId);
    }

    public Courses getCourseById(Long id) {
        return crsRepo.getbycrsid(id);
    }

    public List<SemesterCourses> getCompulsoryCoursesBySemesterId(Long semesterId) {
        return semesterCoursesRepo.getccbysemid(semesterId);
    }

    public List<CourseGroups> getElectiveCoursesBySemesterId(Long semesterId) {
        return courseGroupsRepo.getecbysemid(semesterId);
    }

    public List<Courses> getCourseByCgpId(Long cgpId, Long prgId, Long trmId) {
        return crsRepo.getbycgpId(cgpId, prgId, trmId);
    }

    public List<SemesterCourses> getBacklogCompulsoryCourses(Long studentId, Long semesterId, Long batchId) {
        return semesterCoursesRepo.getBCCourses(studentId, semesterId, batchId);
    }

    public List<SemesterCourses> getBacklogElectiveCourses(Long studentId, Long semesterId, Long batchId) {
        return semesterCoursesRepo.getBECourses(studentId, semesterId, batchId);
    }

    public List<TermCourses> getTermCourses(Long prgId, Long trmId) {
        return trmcrsRepo.getTCourses(prgId, trmId);
    }

    public List<TermCourses> getOptionalCourses(Long semesterId, Long prgId, Long trmId) {
        return trmcrsRepo.getOCourses(semesterId, prgId, trmId);
    }

    public List<TermCourses> getOptionalBacklogCourses(Long studentId, Long semesterId, Long prgId, Long trmId, Long batchId) {
        return trmcrsRepo.getOBCourses(studentId,semesterId, prgId, trmId, batchId);
    }

    public List<TermCourses> getOtherTermCourses(Long studentId, Long semesterId, Long prgId, Long trmId) {
        return trmcrsRepo.getOTermCourses(studentId, semesterId, prgId, trmId);
    }   

    public List<TermCourses> getGradeImprovementCourses(Long studentId, Long prgId, Long trmId) {
        return trmcrsRepo.getGICourses(studentId, prgId, trmId);
    }

    public String getlatestcpi(Long studentId) {
        return stdstrresultRepo.getcpi(studentId);
    }

    public String getlastcpi(Long studentId) {
        return stdstrresultRepo.getlcpi(studentId);
    }

}
