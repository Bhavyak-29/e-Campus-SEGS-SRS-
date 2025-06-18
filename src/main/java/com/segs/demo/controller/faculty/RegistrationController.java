package com.segs.demo.controller.faculty;

import com.segs.demo.model.AcademicYear;
import com.segs.demo.model.Semester;
import com.segs.demo.model.Student;
import com.segs.demo.model.StudentRegistrations;
import com.segs.demo.model.Term;
import com.segs.demo.repository.AcademicYearRepository;
import com.segs.demo.repository.StudentRegistrationCourseRepository;
import com.segs.demo.repository.StudentRegistrationsRepository;
import com.segs.demo.repository.StudentRepository;
import com.segs.demo.repository.TermRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;




import java.util.*;

@Controller
@RequestMapping("/registrations")
public class RegistrationController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentRegistrationsRepository studentRegistrationsRepository;

    @Autowired
    private StudentRegistrationCourseRepository studentRegistrationCourseRepository;

    @Autowired
    private TermRepository termRepository; 

    @Autowired
    private AcademicYearRepository academicYearRepository;

    @GetMapping("/search")
    public String searchStudents(
            @RequestParam(required = false) String fname,
            @RequestParam(required = false) String lname,
            @RequestParam(required = false) String instId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            ModelMap model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Student> studentsPage;

        if (instId != null) {
            studentsPage = studentRepository.findByStdinstidAndStdrowstateGreaterThan(instId, 0, pageable);
        } else if (fname != null && lname != null) {
            studentsPage = studentRepository.findByStdfirstnameContainingIgnoreCaseAndStdlastnameContainingIgnoreCaseAndStdrowstateGreaterThan(fname, lname, 0, pageable);
        } else if (fname != null) {
            studentsPage = studentRepository.findByStdfirstnameContainingIgnoreCaseAndStdrowstateGreaterThan(fname, 0, pageable);
        } else if (lname != null) {
            studentsPage = studentRepository.findByStdlastnameContainingIgnoreCaseAndStdrowstateGreaterThan(lname, 0, pageable);
        } else {
            studentsPage = Page.empty(pageable);
        }

        model.addAttribute("studentsPage", studentsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentsPage.getTotalPages());
        model.addAttribute("totalItems", studentsPage.getTotalElements());

        return "registrations_search";
    }

    @GetMapping("/studentwise/{id}")
    public String showStudentRegistrations(@PathVariable String id, Model model) {

        // Get student object
        List<Student> students = studentRepository.findStudentByInstIdWithLatestRegistration(id);
        if (students.isEmpty()) return "error/404";
        Student student = students.get(0);

        // Get all registrations for student
        List<StudentRegistrations> registrations =
                studentRegistrationsRepository.findAllRegistrationsByStudentIdOrderBySemesterSequence(student.getStdid());

        // Map of registration ID -> list of course detail rows
        Map<Long, List<Object[]>> regCourses = new LinkedHashMap<>();
        for (StudentRegistrations reg : registrations) {
            regCourses.put(reg.getSrgid(), studentRegistrationCourseRepository
                    .findActiveRegistrationCourseDetails(reg.getSrgid()));
        }

        // Pass to view
        model.addAttribute("student", student);
        model.addAttribute("registrations", registrations);
        model.addAttribute("regCourses", regCourses);

        return "registrations_studentwise";
    }

    @GetMapping("/term")
    public String showFacultyCourses(@RequestParam(name = "academicYearId", required = false) Long academicYearId,
                                    @RequestParam(name = "termId", required = false) Long termId,
                                    @RequestParam(name = "semesterId", required = false) String semesterId,
                                    Model model) {

        // 1. Load academic years always
        List<AcademicYear> academicYears = academicYearRepository.findByRowStateGreaterThan(0);
        model.addAttribute("academicYears", academicYears);
        model.addAttribute("academicYearId", academicYearId); // keep selected
        
        
        // 2. Load terms only if academic year is selected
        List<Term> terms = new ArrayList<>();
        if (academicYearId != null) {
            terms = termRepository.findByAcademicYear_Id(academicYearId);
        }
        model.addAttribute("terms", terms);
        model.addAttribute("termId", termId); // keep selected
        
        if (academicYearId != null) {
            AcademicYear selectedYear = academicYearRepository.findById(academicYearId).orElse(null);
            if (selectedYear != null)
                model.addAttribute("ayrname", selectedYear.getName());
            
        }
        
        if (termId != null) {
            Term selectedTerm = termRepository.findById(termId).orElse(null);
            if (selectedTerm != null)
                model.addAttribute("trmname", selectedTerm.getName());
        }

        
        // 3. Load semesters if term is selected
        List<Semester> semesters = new ArrayList<>();
        if (termId != null) {
            semesters = studentRegistrationsRepository.findSemestersByTerm(termId);
        }
        model.addAttribute("semesters", semesters);
        model.addAttribute("semesterId", semesterId); // keep selected

        // 4. Load courses only if term is selected
        if (termId == null) {
            model.addAttribute("message", "Please select an academic year and term.");
            return "term_select";
        }

        List<Object[]> registeredCourses;
        registeredCourses = studentRegistrationCourseRepository.findRegisteredCoursesForTerm(termId);

        model.addAttribute("registeredCourses", registeredCourses);
        return "registered_courses";
    }

    @GetMapping("/courses/{termCourseId}/students")
    public String viewRegisteredStudentsForCourse(@PathVariable Long termCourseId, Model model) {

        List<Object[]> students = studentRegistrationCourseRepository
                .findActiveStudentsByTermCourseId(termCourseId);

        model.addAttribute("students", students);
        return "registered_students"; // simple list table
    }

    @GetMapping("/courses")
    public String showCourseSelectorPage(@RequestParam(required = false) Long academicYearId,
                                        Model model) {

        List<AcademicYear> academicYears = academicYearRepository.findByRowStateGreaterThan(0);
        model.addAttribute("academicYears", academicYears);
        model.addAttribute("academicYearId", academicYearId);

        // Load terms dynamically if academic year selected
        List<Term> terms = new ArrayList<>();
        if (academicYearId != null) {
            terms = termRepository.findByAcademicYear_Id(academicYearId);
            model.addAttribute("terms", terms);
        }

        return "course_selector"; // new Thymeleaf page
    }
    
    @GetMapping("/courses/term")
    public String showCoursesInTerm(
        @RequestParam Long academicYearId,
        @RequestParam Long termId,
        Model model
    ) {
        List<Object[]> registeredCourses = studentRegistrationCourseRepository.findRegisteredCoursesForTerm(termId);
        AcademicYear ay = academicYearRepository.findById(academicYearId).orElse(null);
        Term term = termRepository.findById(termId).orElse(null);

        model.addAttribute("registeredCourses", registeredCourses);
        model.addAttribute("academicYearId", academicYearId);
        model.addAttribute("termId", termId);
        model.addAttribute("ayrname", ay != null ? ay.getName() : "");
        model.addAttribute("trmname", term != null ? term.getName() : "");

        return "course_selection_form"; // course dropdown + submit button
    }

    @GetMapping("/courses/students")
    public String showStudentsForSelectedCourse(@RequestParam Long termCourseId, Model model) {
        List<Object[]> students = studentRegistrationCourseRepository
            .findActiveStudentsByTermCourseId(termCourseId);
        model.addAttribute("students", students);
        return "registered_students";
    }

}
