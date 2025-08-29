package com.ec2.main.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ec2.main.repository.*;
import com.ec2.main.model.*;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CourseDropSaveService {
    @Autowired
    private StudentRegistrationCoursesRepository registrationCoursesRepo;

    @Autowired
    private TermCoursesRepository termCoursesRepo;

    @Autowired
    private SemesterCoursesRepository semesterCoursesRepo;

    @Autowired
    private StudentAdjustmentCoursesRepository studentAdjustmentRepo;

    public void removeCourseIds(List<Long> courseIds, Long srgid, Long termId, Long semesterId, Long studentId) {
        if(courseIds != null) {
            for (Long courseId : courseIds) {
                Long tcrid = termCoursesRepo.findTcrid(Long.valueOf(courseId), termId);
                StudentAdjustmentCourses sacid = studentAdjustmentRepo.findSacid(tcrid.intValue(), srgid);

                System.out.println("strid: " + semesterId);
                System.out.println("crsid: " + courseId);
                System.out.println("tcrid: " + tcrid);
                System.out.println("srgid: " + srgid);

                StudentRegistrationCourses src = registrationCoursesRepo.getSrg(tcrid, srgid);
                if(src==null){
                    System.out.println("NULL Found");
                }
                src.setSrcstatus("DROPPED");
                src.setSrcfield1("ADJUSTMENT");
                src.setSrclastupdatedat(LocalDateTime.now());
                registrationCoursesRepo.save(src);

                if(sacid != null){
                    sacid.setSacstatus("DROPPED");
                    sacid.setSacfield1("ADJUSTMENT");
                    sacid.setSaclastupdatedat(LocalDateTime.now());
                    sacid.setSaclastupdatedby(studentId);
                    studentAdjustmentRepo.save(sacid);
                }
                else{
                    Optional<Long> maxsacid = studentAdjustmentRepo.getMaxId();
                    StudentAdjustmentCourses nsac = new StudentAdjustmentCourses();
                    nsac.setSacid(maxsacid.orElse(0L)+1);
                    nsac.setSacsrcid(src.getSrcid());
                    nsac.setSacsrgid(srgid);
                    nsac.setSactcrid(tcrid.intValue());
                    if ("REGULAR".equals(src.getSrctype())) {
                        nsac.setSactype("REGULAR");
                    } 
                    else if ("AUDIT".equals(src.getSrctype())) {
                        nsac.setSactype("AUDIT");
                    }
                    else if ("GRADE IMPROVEMENT".equals(src.getSrctype())) {
                        nsac.setSactype("GRADE IMPROVEMENT");
                    }
                    else if ("BACKLOG".equals(src.getSrctype())) {
                        nsac.setSactype("BACKLOG");
                    }
                    nsac.setSacscrid(src.getSrcscrid());
                    nsac.setSacstatus("DROPPED");
                    nsac.setSacfield1("ADJUSTMENT");
                    nsac.setSaccreatedby(studentId);
                    nsac.setSaccreatedat(LocalDateTime.now());
                    nsac.setSacrowstate(1);
                    studentAdjustmentRepo.save(nsac);
                }
            }
        }
    }
}
