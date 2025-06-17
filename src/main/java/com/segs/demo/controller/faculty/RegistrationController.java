package com.segs.demo.controller.faculty;

import com.segs.demo.model.Student;
import com.segs.demo.model.StudentRegistrations;
import com.segs.demo.repository.StudentRegistrationCourseRepository;
import com.segs.demo.repository.StudentRegistrationsRepository;
import com.segs.demo.repository.StudentRepository;
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
}
