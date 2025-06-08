package com.segs.demo.controller.faculty;

import com.segs.demo.model.*;
import com.segs.demo.repository.StudentRepository;
import com.segs.demo.service.facultyService;
import com.segs.demo.service.GradeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Controller
public class facultyController {

    @Autowired
    private facultyService facultyService;

    @Autowired
    private StudentRepository studentRepository;

     @Autowired
    private GradeService gradeService;
    // --- Direct Grade Entry (Existing functionality) ---
    @RequestMapping("/directGradeEntry")
    public String directGradeEntry(ModelMap model) {
        
        List<Term> terms = facultyService.getAllTerms();
        List<Egcrstt1> examTypes = facultyService.getAllExamTypes();
        List<AcademicYear> academicYears = facultyService.getAllAcademicYears();
        List<Course> courses = facultyService.getAllCourses();

        model.addAttribute("terms", terms);
        model.addAttribute("examTypes", examTypes);
        model.addAttribute("academicYears", academicYears);
        model.addAttribute("courses", courses);

        return "directGradeEntry";


    }
    @RequestMapping("/directGradeEntry/gradeOptions")
    public String showGradeOptions(
            @RequestParam(required = false) Long CRSID,
            @RequestParam(required = false) Long examTypeId,
            @RequestParam(required = false) List<String> selectedGrades,
            ModelMap model) {
            List<Grade> allGrades = gradeService.getAllGrades();

            List<Grade> distinctGrades = allGrades.stream()
                .collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Grade::getGradeValue))),
                    ArrayList::new
                ));

model.addAttribute("grades", distinctGrades);

model.addAttribute("grades", distinctGrades);
        if (CRSID != null && examTypeId != null) {
            // Fetch students with grades, optionally filter by selected grades
            List<StudentGradeDTO> studentGrades = gradeService.getStudentGrades(CRSID, examTypeId, selectedGrades);
            model.addAttribute("studentGrades", studentGrades);
            model.addAttribute("selectedGrades", selectedGrades);
        }

        model.addAttribute("CRSID", CRSID);
        model.addAttribute("examTypeId", examTypeId);

        return "gradeOptions";
    }
  
    @GetMapping("/students/search")
    public String showStudentSearchForm() {
        return "student_search"; // This will be your search form view (studentSearch.html)
    }

    @PostMapping("/students/search")
    public String searchStudents(
            @RequestParam(required = false) String fname,
            @RequestParam(required = false) String lname,
            @RequestParam(required = false) Long instId,
            ModelMap model) {

        List<Student> students;

        if (instId != null) {
            students = studentRepository.findByStdinstidAndStdrowstateGreaterThan(instId, 0);
        } else if (fname != null && lname != null) {
            students = studentRepository.findByStdfirstnameContainingIgnoreCaseAndStdlastnameContainingIgnoreCaseAndStdrowstateGreaterThan(fname, lname, 0);
        } else if (fname != null) {
            students = studentRepository.findByStdfirstnameContainingIgnoreCaseAndStdrowstateGreaterThan(fname, 0);
        } else if (lname != null) {
            students = studentRepository.findByStdlastnameContainingIgnoreCaseAndStdrowstateGreaterThan(lname, 0);
        } else {
            students = Collections.emptyList();
        }

        model.addAttribute("students", students);
        return "student_search"; // View: search results
    }

    @GetMapping("/students/{id}")
    public String showStudentInfo(@PathVariable Long id, Model model) {
        Student student = studentRepository.findById(id).orElse(null);

        if (student == null) {
            return "error/404"; // Or handle gracefully
        }

        model.addAttribute("student", student);
        return "student_details"; // This view will display full info
    }

    
}
