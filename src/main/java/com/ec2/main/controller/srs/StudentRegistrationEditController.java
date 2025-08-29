package com.ec2.main.controller.srs;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ec2.main.RegistrationConstants;
import com.ec2.main.config.RegistrationDeadlineConfig;
import com.ec2.main.model.Batches;
import com.ec2.main.model.CourseGroups;
import com.ec2.main.model.Courses;
import com.ec2.main.model.Programs;
import com.ec2.main.model.SemesterCourses;
import com.ec2.main.model.Semesters;
import com.ec2.main.model.StudentRegistrationCourses;
import com.ec2.main.model.StudentRegistrations;
import com.ec2.main.model.Students;
import com.ec2.main.model.TermCourses;
import com.ec2.main.model.Terms;
import com.ec2.main.service.StudentRegistrationEditService;
import com.ec2.main.service.StudentRegistrationService;

@Controller
public class StudentRegistrationEditController {

    @Autowired
    private StudentRegistrationService service;

    @Autowired
    private StudentRegistrationEditService editService;

    @Autowired
    private StudentRegistrationService registrationService;

    @Autowired
    private RegistrationDeadlineConfig deadlineConfig;

    @PostMapping("/srs/edit")
    public String editStudentRegistration(
            @RequestParam("stdid") Long studentId,
            @RequestParam("strid") Long semesterId,
            Model model) {

        Students st = service.getStudentById(studentId);
        Semesters str = editService.getSemesterBystrId(semesterId);

        // System.out.println("Semester ID: " + semesterId);

        StudentRegistrations srg = editService.getRegistrationsByStudentIdandSemesterId(studentId, semesterId);

        String cpi;
        if(srg!=null){
            cpi = editService.getlastcpi(studentId);
        }
        else{
            cpi = editService.getlatestcpi(studentId);
        }


        Batches btc = editService.getbatch(str.getStrbchid());
        Programs pr = editService.getprogram(btc.getBchprgid());

        Terms trm = editService.getTerm(str.getStrtrmid());

        model.addAttribute("studentbean", st);
        model.addAttribute("semesterbean", str);
        model.addAttribute("studentRegistrationbean", srg);

        model.addAttribute("batchbean", btc);
        model.addAttribute("programbean", pr);
        model.addAttribute("termbean", trm);

        boolean isWithinDeadline = deadlineConfig.isWithinDeadline();

        Long maxStrid = registrationService.getMaxSemesterId(st.getStdbchid());

        System.out.println("Semester ID: " + semesterId);
        System.out.println("Max Semester ID: " + maxStrid);
        System.out.println("Is Within Deadline: " + isWithinDeadline);

        if(semesterId.shortValue() != maxStrid.shortValue() || !isWithinDeadline) {
            System.out.println("DEBUG: Going to VIEW mode");
            List<StudentRegistrationCourses> strgcrs = new ArrayList<>();
            List<Courses> courses = new ArrayList<>();

            if (srg != null) {
                strgcrs = editService.getStudentRegistrationCourses(srg.getSrgid());

                for (StudentRegistrationCourses src : strgcrs) {
                    Long termcourseId = src.getSrctcrid();
                    
                    TermCourses tc = editService.getTermCourse(termcourseId);
                    Courses course = editService.getCourseById(tc.getTcrcrsid());
                    if (course != null) {
                        courses.add(course);
                    }
                }
                courses.sort(Comparator.comparing(Courses::getCrsname));
            }
            
            model.addAttribute("studentRegistrationCoursesbean", strgcrs);
            model.addAttribute("courses", courses);       
            return RegistrationConstants.JSPSTUDENTREGISTRATIONVIEW;
        }
        else{
            System.out.println("DEBUG: Going to EDIT mode");
            List<StudentRegistrationCourses> strgcrs = new ArrayList<>();
            List<Courses> courses = new ArrayList<>();

            if (srg != null) {
                strgcrs = editService.getStudentRegistrationCourses(srg.getSrgid());

                for (StudentRegistrationCourses src : strgcrs) {
                    Long termcourseId = src.getSrctcrid();
                    
                    TermCourses tc = editService.getTermCourse(termcourseId);
                    Courses course = editService.getCourseById(tc.getTcrcrsid());
                    if (course != null) {
                        courses.add(course);
                    }
                }
                courses.sort(Comparator.comparing(Courses::getCrsname));
            }
            
            model.addAttribute("studentRegistrationCoursesbean", strgcrs);
            model.addAttribute("courses", courses);

            List<Courses> compCourses = new ArrayList<>();
            List<SemesterCourses> compulsoryCourses = editService.getCompulsoryCoursesBySemesterId(semesterId);
            for (SemesterCourses sc : compulsoryCourses) {
                Courses course = editService.getCourseById(sc.getScrcrsid());
                if (course!= null) {
                    compCourses.add(course);
                }
            }
            compCourses.sort(Comparator.comparing(Courses::getCrsname));

            List<Courses> electCourses = new ArrayList<>();
            List<CourseGroups> electiveCourses = editService.getElectiveCoursesBySemesterId(semesterId);
            for (CourseGroups cg : electiveCourses) {
                List<Courses> coursesList = editService.getCourseByCgpId(cg.getCgpid(), pr.getPrgid(), trm.getTrmid());
                if (coursesList != null && !coursesList.isEmpty()) {
                    electCourses.addAll(coursesList);
                }
            }
            electCourses.sort(Comparator.comparing(Courses::getCrsname));

            List<Courses> backlogCCourses = new ArrayList<>();
            List<SemesterCourses> backlogCourses = editService.getBacklogCompulsoryCourses(studentId,semesterId, btc.getBchid());
            for (SemesterCourses sc : backlogCourses) {
                Courses course = editService.getCourseById(sc.getScrcrsid());
                if (course != null) {
                    backlogCCourses.add(course);
                }
            }
            backlogCCourses.sort(Comparator.comparing(Courses::getCrsname));

            List<Courses> backlogECourses = new ArrayList<>();
            List<SemesterCourses> backlogElectiveCourses =editService.getBacklogElectiveCourses(studentId, semesterId, btc.getBchid());
            for (SemesterCourses sc : backlogElectiveCourses) {
                Courses course = editService.getCourseById(sc.getScrcrsid());
                if (course != null) {
                    backlogECourses.add(course);
                }
            }
            backlogECourses.sort(Comparator.comparing(Courses::getCrsname));

            List<Courses> termCourses = new ArrayList<>();
            List<TermCourses> termCrs = editService.getTermCourses(pr.getPrgid(), trm.getTrmid());
            for (TermCourses tc : termCrs) {
                Courses course = editService.getCourseById(tc.getTcrcrsid());
                if (course != null) {
                    termCourses.add(course);
                }
            }
            termCourses.sort(Comparator.comparing(Courses::getCrsname));

            List<Courses> optionalCourses = new ArrayList<>();
            List<TermCourses> optCourses = editService.getOptionalCourses(semesterId, pr.getPrgid(), trm.getTrmid());
            for (TermCourses tc : optCourses) {
                Courses course = editService.getCourseById(tc.getTcrcrsid());
                if (course != null) {
                    optionalCourses.add(course);
                }
            }
            optionalCourses.sort(Comparator.comparing(Courses::getCrsname));

            List<Courses> optionalBacklogCourses = new ArrayList<>();
            List<TermCourses> optBacklogCourses = editService.getOptionalBacklogCourses(studentId, semesterId, pr.getPrgid(), trm.getTrmid(), btc.getBchid());
            for (TermCourses tc : optBacklogCourses) {
                Courses course = editService.getCourseById(tc.getTcrcrsid());
                if (course != null) {
                    optionalBacklogCourses.add(course);
                }
            }
            optionalBacklogCourses.sort(Comparator.comparing(Courses::getCrsname));

            List<Courses> otherTermCourses = new ArrayList<>();
            List<TermCourses> otherTermCrs = editService.getOtherTermCourses(studentId, semesterId,pr.getPrgid(), trm.getTrmid());
            for (TermCourses tc : otherTermCrs) {
                Courses course = editService.getCourseById(tc.getTcrcrsid());
                if (course != null) {
                    otherTermCourses.add(course);
                }
            }
            otherTermCourses.sort(Comparator.comparing(Courses::getCrsname));

            List<Courses> gradeImprovementCourses = new ArrayList<>();
            if (Double.parseDouble(cpi) < 5) {
                List<TermCourses> gradeImprovementCrs = editService.getGradeImprovementCourses(studentId,pr.getPrgid(), trm.getTrmid());
                for (TermCourses tc : gradeImprovementCrs) {
                    Courses course = editService.getCourseById(tc.getTcrcrsid());
                    if (course != null) {
                        gradeImprovementCourses.add(course);
                    }
                }
                gradeImprovementCourses.sort(Comparator.comparing(Courses::getCrsname));
            }

            // model.addAttribute("compulsoryCoursesbean", compulsoryCourses);
            model.addAttribute("cSemesterCoursesbean", compCourses);
            model.addAttribute("eSemesterCoursesbean", electCourses);
            model.addAttribute("bcSemesterCoursesbean", backlogCCourses);
            model.addAttribute("beSemesterCoursesbean", backlogECourses);
            model.addAttribute("termCoursesbean", termCourses);
            model.addAttribute("optionalCoursesbean", optionalCourses);
            model.addAttribute("optionalBacklogCoursesbean", optionalBacklogCourses);
            model.addAttribute("otherTermCoursesbean", otherTermCourses);
            model.addAttribute("gradeImprovementCoursesbean", gradeImprovementCourses);

            return RegistrationConstants.JSPSTUDENTREGISTRATIONEDIT;
        }

    }
}
