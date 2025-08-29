package com.ec2.main.controller.srs;
import com.ec2.main.RegistrationConstants;
import com.ec2.main.config.RegistrationDeadlineConfig;
import com.ec2.main.model.*;
import com.ec2.main.service.*;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SemesterResultViewController {

    @Autowired
    private StudentRegistrationService service;

    @Autowired
    private StudentRegistrationEditService editService;

    @Autowired
    private SemesterResultViewService viewService;

    @PostMapping("/srs/SemesterResult")
    public String editStudentRegistration(
            @RequestParam("stdid") Long studentId,
            @RequestParam("strid") Long semesterId,
            Model model) {

        Students st = service.getStudentById(studentId);
        Semesters str = editService.getSemesterBystrId(semesterId);

        // System.out.println("Semester ID: " + semesterId);

        StudentRegistrations srg = editService.getRegistrationsByStudentIdandSemesterId(studentId, semesterId);

        Batches btc = editService.getbatch(str.getStrbchid());
        Programs pr = editService.getprogram(btc.getBchprgid());

        Terms trm = editService.getTerm(str.getStrtrmid());

        model.addAttribute("studentbean", st);
        model.addAttribute("semesterbean", str);
        model.addAttribute("studentRegistrationbean", srg);

        model.addAttribute("batchbean", btc);
        model.addAttribute("programbean", pr);
        model.addAttribute("termbean", trm);

        List<StudentRegistrationCourses> strgcrs = new ArrayList<>();
        List<Courses> courses = new ArrayList<>();

        Map<Long, List<Object>> courseDataMap = new HashMap<>();

        if (srg != null) {
            strgcrs = editService.getStudentRegistrationCourses(srg.getSrgid());

            for (StudentRegistrationCourses src : strgcrs) {
                Long termcourseId = src.getSrctcrid();

                Egcrstt1 egc = viewService.getObtgrByTcridAndStdid(studentId, termcourseId);
                BigDecimal gradePoints = null;
                String grade = null;

                if(egc != null){
                    gradePoints = egc.getObtainedCredits();
                    grade = viewService.getGradeByObtgr(egc.getObtainedGradeId());
                }

                String remarks = null;
                if (!"REGULAR".equals(src.getSrctype())) {
                    remarks = src.getSrctype();
                }

                TermCourses tc = editService.getTermCourse(termcourseId);
                Courses course = editService.getCourseById(tc.getTcrcrsid());

                if (course != null) {
                    courses.add(course);
                    // Create a list of [grade, gradePoints, remarks]
                    List<Object> data = new ArrayList<>();
                    data.add(grade);
                    data.add(gradePoints);
                    data.add(remarks);

                    courseDataMap.put(course.getCrsid(), data); // Add to map
                }
            }

            courses.sort(Comparator.comparing(Courses::getCrsname));
        }

        for (Map.Entry<Long, List<Object>> entry : courseDataMap.entrySet()) {
            System.out.println("Map key course name: " + entry.getKey());
            System.out.println("Grade: " + entry.getValue().get(0));
        }

        StudentSemesterResult ssr = viewService.getSsrBySrg(srg.getSrgid());
        
        model.addAttribute("studentRegistrationCoursesbean", strgcrs);
        model.addAttribute("courses", courses);
        model.addAttribute("map", courseDataMap);
        model.addAttribute("result", ssr);
        return RegistrationConstants.JSPCOURSERESULTVIEW;
    }
}
