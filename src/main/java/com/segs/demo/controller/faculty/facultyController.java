package com.segs.demo.controller.faculty;

import com.segs.demo.model.*;
import com.segs.demo.service.facultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class facultyController {

    @Autowired
    private facultyService facultyService;

    @RequestMapping("/directGradeEntry")
    public String directGradeEntry(ModelMap model) {
        List<Term> terms = facultyService.getAllTerms();
        List<ExamType> examTypes = facultyService.getAllExamTypes();
        List<AcademicYear> academicYears = facultyService.getAllAcademicYears();
        List<Course> courses = facultyService.getAllCourses();

        model.addAttribute("terms", terms);
        model.addAttribute("examTypes", examTypes);
        model.addAttribute("academicYears", academicYears);
        model.addAttribute("courses", courses);

        return "directGradeEntry"; // Thymeleaf HTML file
    }
}
