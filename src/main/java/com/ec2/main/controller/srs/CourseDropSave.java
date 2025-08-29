package com.ec2.main.controller.srs;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ec2.main.RegistrationConstants;
import com.ec2.main.model.StudentRegistrations;
import com.ec2.main.service.CourseDropSaveService;
import com.ec2.main.service.StudentRegistrationEditService;

@Controller
public class CourseDropSave {
    @Autowired
    private StudentRegistrationEditService editService;

    @Autowired
    private CourseDropSaveService courseDropSaveService;

    @PostMapping("/srs/CourseDropSave")
    public String saveCourseDrop(
            @RequestParam("studentId") Long studentId,
            @RequestParam("semesterId") Long semesterId,
            @RequestParam("termId") Long termId,
            @RequestParam("semesterName") String semesterName,
            @RequestParam("courseIdsBefore") List<Long> courseIdsBefore,
            @RequestParam(value = "courseIds", required = false) List<Long> courseIds,
            Model model) {

        StudentRegistrations srg = editService.getRegistrationsByStudentIdandSemesterId(studentId, semesterId);
        Long srgid = srg.getSrgid();

        System.out.println("Student ID: " + studentId);
        System.out.println("Semester ID: " + semesterId);
        System.out.println("Term ID: " + termId);
        System.out.println("Semester Name: " + semesterName);
        System.out.println("Selected Course IDs to RETAIN:");
        if (courseIds != null) {
            for (Long id : courseIds) {
                System.out.println(id);
            }
        } else {
            System.out.println("No courses selected. All eligible courses dropped.");
        }

        System.out.println("Course IDs before drop:");
        for (Long id : courseIdsBefore) {
            System.out.println(id);
        }


        if (courseIds == null) {
            courseIds = new ArrayList<>();
        }

        Set<Long> beforeSet = new HashSet<>(courseIdsBefore);
        Set<Long> retainSet = new HashSet<>(courseIds);

        beforeSet.removeAll(retainSet);
        List<Long> dropCourseIds = new ArrayList<>(beforeSet);

        System.out.println("Courses to Drop:");
        for(Long id: dropCourseIds){
            System.out.println(id);
        }

        courseDropSaveService.removeCourseIds(dropCourseIds, srgid, termId, semesterId, studentId);

        model.addAttribute("semesterName", semesterName);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("studentId", studentId);

        return RegistrationConstants.JSPCOURSEDROPSAVE;  
    }
}
