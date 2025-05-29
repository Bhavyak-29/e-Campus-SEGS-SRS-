package com.segs.demo.controller.faculty;

import com.segs.demo.model.*;
import com.segs.demo.repository.StudentRepository;
import com.segs.demo.service.facultyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class facultyController {

    @Autowired
    private facultyService facultyService;

    @Autowired
    private StudentRepository studentRepository;

    // --- Direct Grade Entry (Existing functionality) ---
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

        return "directGradeEntry";
    }

    // --- Student Info: Search Form View ---
    @GetMapping("/student-search")
    public String showSearchForm() {
        return "student_search"; // This is the form view
    }

    // --- Student Info: Search Result Handling ---
    @GetMapping("/student-info")
    public String getStudentInfo(
            @RequestParam String branch,
            @RequestParam String batch,
            @RequestParam String section,
            Model model) {

        List<Student> students = studentRepository.findByBranchAndBatchAndSection(branch, batch, section);
        model.addAttribute("students", students);

        return "student_search"; // Return same view with results
    }
}
