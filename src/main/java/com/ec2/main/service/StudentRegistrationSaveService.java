package com.ec2.main.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.ec2.main.model.*;
import com.ec2.main.repository.*;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class StudentRegistrationSaveService {

    @Autowired
    private StudentRegistrationCoursesRepository registrationCoursesRepo;

    @Autowired
    private StudentRegistrationsRepository registrationRepo;

    @Autowired
    private TermCoursesRepository termCoursesRepo;

    @Autowired
    private SemesterCoursesRepository semesterCoursesRepo;

    public void deleteSrcBySrg(Long srgid) {
        registrationCoursesRepo.deleteBySrcsrgid(srgid);
    }

    public Long newRegistration(Long studentId, Long semesterId) {
        StudentRegistrations registration = new StudentRegistrations();
        Optional<Long> maxSrgid = registrationRepo.findMaxSrgid();
        registration.setSrgid(maxSrgid.orElse(0L) + 1);
        registration.setSrgstdid(studentId);
        registration.setSrgstrid(semesterId);
        registration.setSrgregdate(LocalDate.now());
        registration.setSrgcreatedat(LocalDateTime.now());
        registration.setSrgcreatedby(7L);
        registration.setSrglastupdatedat(LocalDateTime.now());
        registration.setSrgrowstate((long) 1);

        registrationRepo.save(registration);

        return maxSrgid.orElse(0L) + 1;
    }

    public void selectedCourseIds(MultiValueMap<String, String> allParams, Long srgid, Long termId, Long semesterId) {
        List<String> selectedCourseIds = allParams.get("selectedCourseIds");
        if(selectedCourseIds != null) {
            for (String courseId : selectedCourseIds) {
                StudentRegistrationCourses src = new StudentRegistrationCourses();
                src.setSrcsrgid(srgid); // Use the same srgid as registration
                src.setSrctype("REGULAR");
                src.setSrcstatus("ACTIVE");
                src.setSrccreatedby(7L);
                src.setSrccreatedat(LocalDateTime.now());
                src.setSrcrowstate((long) 1);

                Long tcrid = termCoursesRepo.findTcrid(Long.parseLong(courseId), termId);
                Long scrid = semesterCoursesRepo.findScrid(semesterId, Long.parseLong(courseId));
                Optional<Long> srcid = registrationCoursesRepo.findMaxSrcid();
                src.setSrctcrid(tcrid);
                src.setSrcscrid(scrid);
                src.setSrcid(srcid.orElse(0L) + 1);

                registrationCoursesRepo.save(src);
            }
        }
    }

    public void selectedOtherCourseIds(MultiValueMap<String, String> allParams, Long srgid, Long termId, Long semesterId) {
        List<String> selectedOtherCourseIds = allParams.get("selectedOtherCourses");
        if(selectedOtherCourseIds != null) {
            for (String courseId : selectedOtherCourseIds) {
                StudentRegistrationCourses src = new StudentRegistrationCourses();
                src.setSrcsrgid(srgid); // Use the same srgid as registration
                src.setSrctype("REGULAR");
                src.setSrcstatus("ACTIVE");
                src.setSrccreatedby(7L);
                src.setSrccreatedat(LocalDateTime.now());
                src.setSrcrowstate((long) 1);

                Long tcrid = termCoursesRepo.findTcrid(Long.parseLong(courseId), termId);
                Long scrid = semesterCoursesRepo.findScrid(semesterId, Long.parseLong(courseId));
                Optional<Long> srcid = registrationCoursesRepo.findMaxSrcid();
                src.setSrctcrid(tcrid);
                src.setSrcscrid(scrid);
                src.setSrcid(srcid.orElse(0L) + 1);

                registrationCoursesRepo.save(src);
            }
        }
    }

    public void selectedGradeImprovementCourseIds(MultiValueMap<String, String> allParams, Long srgid, Long termId, Long semesterId) {
        List<String> selectedGICourseIds = allParams.get("selectedGradeImprovementCourses");
        if(selectedGICourseIds != null) {
            for (String courseId : selectedGICourseIds) {
                StudentRegistrationCourses src = new StudentRegistrationCourses();
                src.setSrcsrgid(srgid); // Use the same srgid as registration
                src.setSrctype("GRADE IMPROVEMENT");
                src.setSrcstatus("ACTIVE");
                src.setSrccreatedby(7L);
                src.setSrccreatedat(LocalDateTime.now());
                src.setSrcrowstate((long) 1);

                Long tcrid = termCoursesRepo.findTcrid(Long.parseLong(courseId), termId);
                Long scrid = semesterCoursesRepo.findScrid(semesterId, Long.parseLong(courseId));
                Optional<Long> srcid = registrationCoursesRepo.findMaxSrcid();
                src.setSrctcrid(tcrid);
                src.setSrcscrid(scrid);
                src.setSrcid(srcid.orElse(0L) + 1);

                registrationCoursesRepo.save(src);
            }
        }
    }

    public void selectedBacklogCourseIds(MultiValueMap<String, String> allParams, Long srgid, Long termId, Long semesterId) {
        List<String> selectedBacklogCourseIds = allParams.get("selectedBacklogCourses");
        if(selectedBacklogCourseIds != null) {
            for (String courseId : selectedBacklogCourseIds) {
                StudentRegistrationCourses src = new StudentRegistrationCourses();
                src.setSrcsrgid(srgid); // Use the same srgid as registration
                src.setSrctype("BACKLOG");
                src.setSrcstatus("ACTIVE");
                src.setSrccreatedby(7L);
                src.setSrccreatedat(LocalDateTime.now());
                src.setSrcrowstate((long) 1);

                Long tcrid = termCoursesRepo.findTcrid(Long.parseLong(courseId), termId);
                Long scrid = semesterCoursesRepo.findScrid(semesterId, Long.parseLong(courseId));
                Optional<Long> srcid = registrationCoursesRepo.findMaxSrcid();
                src.setSrctcrid(tcrid);
                src.setSrcscrid(scrid);
                src.setSrcid(srcid.orElse(0L) + 1);

                registrationCoursesRepo.save(src);
            }
        }
    }

    public void selectedAuditCourseIds(MultiValueMap<String, String> allParams, Long srgid, Long termId, Long semesterId) {
        List<String> selectedAuditCourseIds = allParams.get("selectedAuditCourses");
        if(selectedAuditCourseIds != null) {
            for (String courseId : selectedAuditCourseIds) {
                StudentRegistrationCourses src = new StudentRegistrationCourses();
                src.setSrcsrgid(srgid); // Use the same srgid as registration
                src.setSrctype("AUDIT");
                src.setSrcfield2("Pass/Not Pass Course");
                src.setSrcstatus("ACTIVE");
                src.setSrccreatedby(7L);
                src.setSrccreatedat(LocalDateTime.now());
                src.setSrcrowstate((long) 1);

                Long tcrid = termCoursesRepo.findTcrid(Long.parseLong(courseId), termId);
                Long scrid = semesterCoursesRepo.findScrid(semesterId, Long.parseLong(courseId));
                Optional<Long> srcid = registrationCoursesRepo.findMaxSrcid();
                src.setSrctcrid(tcrid);
                src.setSrcscrid(scrid);
                src.setSrcid(srcid.orElse(0L) + 1);

                registrationCoursesRepo.save(src);
            }
        }
    }

}
