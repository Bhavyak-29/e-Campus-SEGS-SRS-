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
public class CourseAddDropSaveService {

    @Autowired
    private StudentRegistrationCoursesRepository registrationCoursesRepo;

    @Autowired
    private TermCoursesRepository termCoursesRepo;

    @Autowired
    private SemesterCoursesRepository semesterCoursesRepo;

    @Autowired
    private StudentAdjustmentCoursesRepository studentAdjustmentRepo;

    public void removeRegularCourseIds(Set<String> courseIds, Long srgid, Long termId, Long semesterId, Long studentId) {
        if(courseIds != null) {
            for (String courseId : courseIds) {
                Long tcrid = termCoursesRepo.findTcrid(Long.parseLong(courseId), termId);
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
                    nsac.setSactype("REGULAR");
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

    public void removeBacklogCourseIds(Set<String> courseIds, Long srgid, Long termId, Long semesterId, Long studentId) {
        if(courseIds != null) {
            for (String courseId : courseIds) {
                Long tcrid = termCoursesRepo.findTcrid(Long.parseLong(courseId), termId);
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
                    nsac.setSactype("BACKLOG");
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

    public void removeGradeImprovementCourseIds(Set<String> courseIds, Long srgid, Long termId, Long semesterId, Long studentId) {
        if(courseIds != null) {
            for (String courseId : courseIds) {
                Long tcrid = termCoursesRepo.findTcrid(Long.parseLong(courseId), termId);
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
                    nsac.setSactype("GRADE IMPROVEMENT");
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

    public void removeAuditCourseIds(Set<String> courseIds, Long srgid, Long termId, Long semesterId, Long studentId) {
        if(courseIds != null) {
            for (String courseId : courseIds) {
                Long tcrid = termCoursesRepo.findTcrid(Long.parseLong(courseId), termId);
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
                    nsac.setSactype("AUDIT");
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

    public void addNewRegularCourseIds(Set<String> courseIds, Long srgid, Long termId, Long semesterId, Long studentId) {
        if(courseIds != null) {
            for (String courseId : courseIds) {
                Integer scrid = semesterCoursesRepo.findScrid(semesterId, Long.parseLong(courseId));
                Long tcrid = termCoursesRepo.findTcrid(Long.parseLong(courseId), termId);
                StudentAdjustmentCourses sacid = studentAdjustmentRepo.findSacid(tcrid.intValue(), srgid);

                StudentRegistrationCourses src = registrationCoursesRepo.getSrg(tcrid, srgid);
                Long srcid;

                if(src != null){
                    srcid=src.getSrcid();
                    src.setSrctype("REGULAR");
                    src.setSrcstatus("ACTIVE");
                    src.setSrcfield1("ADDED");
                    src.setSrclastupdatedat(LocalDateTime.now());
                    src.setSrclastupdatedby(20L);
                    src.setSrclastupdatedat(LocalDateTime.now());
                    registrationCoursesRepo.save(src);
                }
                else{
                    Optional<Long> id = registrationCoursesRepo.findMaxSrcid();
                    srcid=id.orElse(0L) + 1;
                    StudentRegistrationCourses nsrc = new StudentRegistrationCourses();

                    nsrc.setSrcid(srcid);
                    nsrc.setSrcsrgid(srgid); // Use the same srgid as registration
                    nsrc.setSrctcrid(tcrid);
                    nsrc.setSrctype("REGULAR");
                    nsrc.setSrcscrid(scrid);
                    nsrc.setSrcstatus("ACTIVE");
                    nsrc.setSrcfield1("ADDED");
                    nsrc.setSrccreatedby(7L);
                    nsrc.setSrccreatedat(LocalDateTime.now());
                    nsrc.setSrcrowstate((long) 1);
                    
                    registrationCoursesRepo.save(nsrc);
                }

                if(sacid != null){
                    sacid.setSactype("REGULAR");
                    sacid.setSacstatus("ACTIVE");
                    sacid.setSacfield1("ADDED");
                    sacid.setSaclastupdatedat(LocalDateTime.now());
                    sacid.setSaclastupdatedby(studentId);
                    studentAdjustmentRepo.save(sacid);
                }
                else{
                    Optional<Long> maxsacid = studentAdjustmentRepo.getMaxId();
                    StudentAdjustmentCourses nsac = new StudentAdjustmentCourses();
                    nsac.setSacid(maxsacid.orElse(0L)+1);
                    nsac.setSacsrcid(srcid);
                    nsac.setSacsrgid(srgid);
                    nsac.setSactcrid(tcrid.intValue());
                    nsac.setSactype("REGULAR");
                    nsac.setSacscrid(scrid);
                    nsac.setSacstatus("ACTIVE");
                    nsac.setSacfield1("ADDED");
                    nsac.setSaccreatedby(studentId);
                    nsac.setSaccreatedat(LocalDateTime.now());
                    nsac.setSacrowstate(1);
                    studentAdjustmentRepo.save(nsac);
                }
            }
        }
    }

    public void addNewBacklogCourseIds(Set<String> courseIds, Long srgid, Long termId, Long semesterId, Long studentId) {
        if(courseIds != null) {
            for (String courseId : courseIds) {
                Integer scrid = semesterCoursesRepo.findScrid(semesterId, Long.parseLong(courseId));
                Long tcrid = termCoursesRepo.findTcrid(Long.parseLong(courseId), termId);
                StudentAdjustmentCourses sacid = studentAdjustmentRepo.findSacid(tcrid.intValue(), srgid);

                StudentRegistrationCourses src = registrationCoursesRepo.getSrg(tcrid, srgid);
                Long srcid;

                if(src != null){
                    srcid=src.getSrcid();
                    src.setSrctype("BACKLOG");
                    src.setSrcstatus("ACTIVE");
                    src.setSrcfield1("ADDED");
                    src.setSrclastupdatedat(LocalDateTime.now());
                    src.setSrclastupdatedby(20L);
                    src.setSrclastupdatedat(LocalDateTime.now());
                    registrationCoursesRepo.save(src);
                }
                else{
                    Optional<Long> id = registrationCoursesRepo.findMaxSrcid();
                    srcid=id.orElse(0L) + 1;
                    StudentRegistrationCourses nsrc = new StudentRegistrationCourses();

                    nsrc.setSrcid(srcid);
                    nsrc.setSrcsrgid(srgid); // Use the same srgid as registration
                    nsrc.setSrctcrid(tcrid);
                    nsrc.setSrctype("BACKLOG");
                    nsrc.setSrcscrid(scrid);
                    nsrc.setSrcstatus("ACTIVE");
                    nsrc.setSrcfield1("ADDED");
                    nsrc.setSrccreatedby(7L);
                    nsrc.setSrccreatedat(LocalDateTime.now());
                    nsrc.setSrcrowstate((long) 1);
                    
                    registrationCoursesRepo.save(nsrc);
                }

                if(sacid != null){
                    sacid.setSactype("BACKLOG");
                    sacid.setSacstatus("ACTIVE");
                    sacid.setSacfield1("ADDED");
                    sacid.setSaclastupdatedat(LocalDateTime.now());
                    sacid.setSaclastupdatedby(studentId);
                    studentAdjustmentRepo.save(sacid);
                }
                else{
                    Optional<Long> maxsacid = studentAdjustmentRepo.getMaxId();
                    StudentAdjustmentCourses nsac = new StudentAdjustmentCourses();
                    nsac.setSacid(maxsacid.orElse(0L)+1);
                    nsac.setSacsrcid(srcid);
                    nsac.setSacsrgid(srgid);
                    nsac.setSactcrid(tcrid.intValue());
                    nsac.setSactype("BACKLOG");
                    nsac.setSacscrid(scrid);
                    nsac.setSacstatus("ACTIVE");
                    nsac.setSacfield1("ADDED");
                    nsac.setSaccreatedby(studentId);
                    nsac.setSaccreatedat(LocalDateTime.now());
                    nsac.setSacrowstate(1);
                    studentAdjustmentRepo.save(nsac);
                }
            }
        }
    }

    public void addNewGradeImprovementCourseIds(Set<String> courseIds, Long srgid, Long termId, Long semesterId, Long studentId) {
        if(courseIds != null) {
            for (String courseId : courseIds) {
                Integer scrid = semesterCoursesRepo.findScrid(semesterId, Long.parseLong(courseId));
                Long tcrid = termCoursesRepo.findTcrid(Long.parseLong(courseId), termId);
                StudentAdjustmentCourses sacid = studentAdjustmentRepo.findSacid(tcrid.intValue(), srgid);

                StudentRegistrationCourses src = registrationCoursesRepo.getSrg(tcrid, srgid);
                Long srcid;

                if(src != null){
                    srcid=src.getSrcid();
                    src.setSrctype("GRADE IMPROVEMENT");
                    src.setSrcstatus("ACTIVE");
                    src.setSrcfield1("ADDED");
                    src.setSrclastupdatedat(LocalDateTime.now());
                    src.setSrclastupdatedby(20L);
                    src.setSrclastupdatedat(LocalDateTime.now());
                    registrationCoursesRepo.save(src);
                }
                else{
                    Optional<Long> id = registrationCoursesRepo.findMaxSrcid();
                    srcid=id.orElse(0L) + 1;
                    StudentRegistrationCourses nsrc = new StudentRegistrationCourses();

                    nsrc.setSrcid(srcid);
                    nsrc.setSrcsrgid(srgid); // Use the same srgid as registration
                    nsrc.setSrctcrid(tcrid);
                    nsrc.setSrctype("GRADE IMPROVEMENT");
                    nsrc.setSrcscrid(scrid);
                    nsrc.setSrcstatus("ACTIVE");
                    nsrc.setSrcfield1("ADDED");
                    nsrc.setSrccreatedby(7L);
                    nsrc.setSrccreatedat(LocalDateTime.now());
                    nsrc.setSrcrowstate((long) 1);
                    
                    registrationCoursesRepo.save(nsrc);
                }

                if(sacid != null){
                    sacid.setSactype("GRADE IMPROVEMENT");
                    sacid.setSacstatus("ACTIVE");
                    sacid.setSacfield1("ADDED");
                    sacid.setSaclastupdatedat(LocalDateTime.now());
                    sacid.setSaclastupdatedby(studentId);
                    studentAdjustmentRepo.save(sacid);
                }
                else{
                    Optional<Long> maxsacid = studentAdjustmentRepo.getMaxId();
                    StudentAdjustmentCourses nsac = new StudentAdjustmentCourses();
                    nsac.setSacid(maxsacid.orElse(0L)+1);
                    nsac.setSacsrcid(srcid);
                    nsac.setSacsrgid(srgid);
                    nsac.setSactcrid(tcrid.intValue());
                    nsac.setSactype("GRADE IMPROVEMENT");
                    nsac.setSacscrid(scrid);
                    nsac.setSacstatus("ACTIVE");
                    nsac.setSacfield1("ADDED");
                    nsac.setSaccreatedby(studentId);
                    nsac.setSaccreatedat(LocalDateTime.now());
                    nsac.setSacrowstate(1);
                    studentAdjustmentRepo.save(nsac);
                }
            }
        }
    }

    public void addNewAuditCourseIds(Set<String> courseIds, Long srgid, Long termId, Long semesterId, Long studentId) {
        if(courseIds != null) {
            for (String courseId : courseIds) {
                Integer scrid = semesterCoursesRepo.findScrid(semesterId, Long.parseLong(courseId));
                Long tcrid = termCoursesRepo.findTcrid(Long.parseLong(courseId), termId);
                StudentAdjustmentCourses sacid = studentAdjustmentRepo.findSacid(tcrid.intValue(), srgid);

                StudentRegistrationCourses src = registrationCoursesRepo.getSrg(tcrid, srgid);
                Long srcid;

                if(src != null){
                    srcid=src.getSrcid();
                    src.setSrctype("AUDIT");
                    src.setSrcstatus("ACTIVE");
                    src.setSrcfield1("ADDED");
                    src.setSrclastupdatedat(LocalDateTime.now());
                    src.setSrclastupdatedby(20L);
                    src.setSrclastupdatedat(LocalDateTime.now());
                    registrationCoursesRepo.save(src);
                }
                else{
                    Optional<Long> id = registrationCoursesRepo.findMaxSrcid();
                    srcid = id.orElse(0L) + 1;
                    StudentRegistrationCourses nsrc = new StudentRegistrationCourses();

                    nsrc.setSrcid(srcid);
                    nsrc.setSrcsrgid(srgid); // Use the same srgid as registration
                    nsrc.setSrctcrid(tcrid);
                    nsrc.setSrctype("AUDIT");
                    nsrc.setSrcscrid(scrid);
                    nsrc.setSrcstatus("ACTIVE");
                    nsrc.setSrcfield1("ADDED");
                    nsrc.setSrcfield2("Pass/Not Pass Course");
                    nsrc.setSrccreatedby(7L);
                    nsrc.setSrccreatedat(LocalDateTime.now());
                    nsrc.setSrcrowstate((long) 1);
                    
                    registrationCoursesRepo.save(nsrc);
                }

                if(sacid != null){
                    sacid.setSactype("AUDIT");
                    sacid.setSacstatus("ACTIVE");
                    sacid.setSacfield1("ADDED");
                    sacid.setSaclastupdatedat(LocalDateTime.now());
                    sacid.setSaclastupdatedby(studentId);
                    studentAdjustmentRepo.save(sacid);
                }
                else{
                    Optional<Long> maxsacid = studentAdjustmentRepo.getMaxId();
                    StudentAdjustmentCourses nsac = new StudentAdjustmentCourses();
                    nsac.setSacid(maxsacid.orElse(0L)+1);
                    nsac.setSacsrcid(srcid);
                    nsac.setSacsrgid(srgid);
                    nsac.setSactcrid(tcrid.intValue());
                    nsac.setSactype("AUDIT");
                    nsac.setSacscrid(scrid);
                    nsac.setSacstatus("ACTIVE");
                    nsac.setSacfield1("ADDED");
                    nsac.setSaccreatedby(studentId);
                    nsac.setSaccreatedat(LocalDateTime.now());
                    nsac.setSacrowstate(1);
                    studentAdjustmentRepo.save(nsac);
                }
            }
        }
    }

}
