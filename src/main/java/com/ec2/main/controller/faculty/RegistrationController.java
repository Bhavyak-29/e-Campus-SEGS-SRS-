package com.ec2.main.controller.faculty;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.ec2.main.model.AcademicYears;
import com.ec2.main.model.Semesters;
import com.ec2.main.model.StudentRegistration;
import com.ec2.main.model.Students;
import com.ec2.main.model.Terms;
import com.ec2.main.repository.AcademicYearsRepository;
import com.ec2.main.repository.StudentRegistrationCoursesRepository;
import com.ec2.main.repository.StudentRegistrationRepository;
import com.ec2.main.repository.StudentsRepository;
import com.ec2.main.repository.TermsRepository;

@Controller
@RequestMapping("/registrations")
public class RegistrationController {

    @Autowired
    private StudentsRepository studentsRepository;

    @Autowired
    private StudentRegistrationRepository studentRegistrationRepository;

    @Autowired
    private StudentRegistrationCoursesRepository studentRegistrationCoursesRepository;

    @Autowired
    private TermsRepository termsRepository; 

    @Autowired
    private AcademicYearsRepository academicYearsRepository;

    @GetMapping("/search")
    public String searchStudents(
            @RequestParam(required = false) String fname,
            @RequestParam(required = false) String lname,
            @RequestParam(required = false) String instId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            ModelMap model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Students> studentsPage;

        if (instId != null) {
            studentsPage = studentsRepository.findByStdinstidAndStdrowstateGreaterThan(instId, 0, pageable);
        } else if (fname != null && lname != null) {
            studentsPage = studentsRepository.findByStdfirstnameContainingIgnoreCaseAndStdlastnameContainingIgnoreCaseAndStdrowstateGreaterThan(fname, lname, 0, pageable);
        } else if (fname != null) {
            studentsPage = studentsRepository.findByStdfirstnameContainingIgnoreCaseAndStdrowstateGreaterThan(fname, 0, pageable);
        } else if (lname != null) {
            studentsPage = studentsRepository.findByStdlastnameContainingIgnoreCaseAndStdrowstateGreaterThan(lname, 0, pageable);
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
        List<Students> students = studentsRepository.findStudentByInstIdWithLatestRegistration(id);
        if (students.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        Students student = students.get(0);

        // Get all registrations for student
        List<StudentRegistration> registrations =
                studentRegistrationRepository.findAllRegistrationsByStudentIdOrderBySemesterSequence(student.getStdid());

        // Map of registration ID -> list of course detail rows
        Map<Long, List<Object[]>> regCourses = new LinkedHashMap<>();
        for (StudentRegistration reg : registrations) {
            regCourses.put(reg.getSrgid(), studentRegistrationCoursesRepository
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
        List<AcademicYears> academicYears = academicYearsRepository.findByAyrrowstateGreaterThan(0);
        model.addAttribute("academicYears", academicYears);
        model.addAttribute("academicYearId", academicYearId); // keep selected
        
        
        // 2. Load terms only if academic year is selected
        List<Terms> terms = new ArrayList<>();
        if (academicYearId != null) {
            terms = termsRepository.findByAcademicYear_Ayrid(academicYearId);
        }
        model.addAttribute("terms", terms);
        model.addAttribute("termId", termId); // keep selected
        
        if (academicYearId != null) {
            AcademicYears selectedYear = academicYearsRepository.findById(academicYearId).orElse(null);
            if (selectedYear != null)
                model.addAttribute("ayrname", selectedYear.getAyrname());
            
        }
        
        if (termId != null) {
            Terms selectedTerm = termsRepository.findById(termId).orElse(null);
            if (selectedTerm != null)
                model.addAttribute("trmname", selectedTerm.getTrmname());
        }

        
        // 3. Load semesters if term is selected
        List<Semesters> semesters = new ArrayList<>();
        if (termId != null) {
            semesters = studentRegistrationRepository.findSemestersByTerm(termId);
        }
        model.addAttribute("semesters", semesters);
        model.addAttribute("semesterId", semesterId); // keep selected

        // 4. Load courses only if term is selected
        if (termId == null) {
            model.addAttribute("message", "Please select an academic year and term.");
            return "term_select";
        }

        List<Object[]> registeredCourses;
        registeredCourses = studentRegistrationCoursesRepository.findRegisteredCoursesForTerm(termId);

        model.addAttribute("registeredCourses", registeredCourses);
        return "registered_courses";
    }

    @GetMapping("/courses/{termCourseId}/students")
    public String viewRegisteredStudentsForCourse(@PathVariable Long termCourseId, Model model) {

        List<Object[]> students = studentRegistrationCoursesRepository
                .findActiveStudentsByTermCourseId(termCourseId);

        model.addAttribute("students", students);
        return "registered_students"; // simple list table
    }

    @GetMapping("/courses")
    public String showCourseSelectorPage(@RequestParam(required = false) Long academicYearId,
                                        Model model) {

        List<AcademicYears> academicYears = academicYearsRepository.findByAyrrowstateGreaterThan(0);
        model.addAttribute("academicYears", academicYears);
        model.addAttribute("academicYearId", academicYearId);

        // Load terms dynamically if academic year selected
        List<Terms> terms = new ArrayList<>();
        if (academicYearId != null) {
            terms = termsRepository.findByAcademicYear_Ayrid(academicYearId);
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
        List<Object[]> registeredCourses = studentRegistrationCoursesRepository.findRegisteredCoursesForTerm(termId);
        AcademicYears ay = academicYearsRepository.findById(academicYearId).orElse(null);
        Terms term = termsRepository.findById(termId).orElse(null);

        model.addAttribute("registeredCourses", registeredCourses);
        model.addAttribute("academicYearId", academicYearId);
        model.addAttribute("termId", termId);
        model.addAttribute("ayrname", ay != null ? ay.getAyrname() : "");
        model.addAttribute("trmname", term != null ? term.getTrmname() : "");

        return "course_selection_form"; // course dropdown + submit button
    }

    @GetMapping("/courses/students")
    public String showStudentsForSelectedCourse(@RequestParam Long termCourseId, Model model) {
        List<Object[]> students = studentRegistrationCoursesRepository
            .findActiveStudentsByTermCourseId(termCourseId);
        model.addAttribute("students", students);
        return "registered_students";
    }

}
