// ResultController.java
package com.segs.demo.controller.faculty; // Adjust package as per your project structure

import com.segs.demo.model.Egcrstt1; // Adjust imports based on your model paths
import com.segs.demo.model.Eggradm1;
import com.segs.demo.model.Student;
import com.segs.demo.model.StudentRegistrations;
import com.segs.demo.model.StudentSemesterResult;
import com.segs.demo.repository.Egcrstt1Repository;
import com.segs.demo.repository.Eggradm1Repository;
import com.segs.demo.repository.StudentRegistrationCourseRepository;
import com.segs.demo.repository.StudentRegistrationsRepository;
import com.segs.demo.repository.StudentRepository;
import com.segs.demo.repository.StudentSemesterResultRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ResultController {

   @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private Egcrstt1Repository egcrstt1Repository; // Renamed for consistency

    @Autowired
    private Eggradm1Repository eggradm1Repository; // Renamed for consistency

    @Autowired
    private StudentRegistrationsRepository studentRegistrationsRepository; // New autowired field

    @Autowired
    private StudentRegistrationCourseRepository studentRegistrationCourseRepository; // New autowired field

    @Autowired
    private StudentSemesterResultRepository studentSemesterResultRepository; // New autowired field

    /**
     * Handles the search for students to view their results.
     * Takes optional parameters for first name, last name, and institution ID.
     * Displays a paginated list of students matching the criteria.
     *
     * @param fname Optional first name for searching.
     * @param lname Optional last name for searching.
     * @param instId Optional institution ID for searching.
     * @param page Current page number for pagination (defaults to 0).
     * @param size Number of items per page for pagination (defaults to 5).
     * @param model ModelMap to add attributes for the Thymeleaf view.
     * @return The name of the Thymeleaf template for student results search.
     */
    @GetMapping("/results/studentwise/search")
    public String searchStudentsForResults(
            @RequestParam(required = false) String fname,
            @RequestParam(required = false) String lname,
            @RequestParam(required = false) String instId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            ModelMap model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Student> studentsPage;

        // Logic to find students based on provided criteria, similar to your existing student search
        if (instId != null && !instId.isEmpty()) {
            studentsPage = studentRepository.findByStdinstidAndStdrowstateGreaterThan(instId, 0, pageable);
        } else if (fname != null && !fname.isEmpty() && lname != null && !lname.isEmpty()) {
            studentsPage = studentRepository.findByStdfirstnameContainingIgnoreCaseAndStdlastnameContainingIgnoreCaseAndStdrowstateGreaterThan(fname, lname, 0, pageable);
        } else if (fname != null && !fname.isEmpty()) {
            studentsPage = studentRepository.findByStdfirstnameContainingIgnoreCaseAndStdrowstateGreaterThan(fname, 0, pageable);
        } else if (lname != null && !lname.isEmpty()) {
            studentsPage = studentRepository.findByStdlastnameContainingIgnoreCaseAndStdrowstateGreaterThan(lname, 0, pageable);
        } else {
            // If no search criteria, return an empty page to start
            studentsPage = Page.empty(pageable);
        }

        model.addAttribute("studentsPage", studentsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentsPage.getTotalPages());
        model.addAttribute("totalItems", studentsPage.getTotalElements());
        model.addAttribute("fname", fname); // Keep search params for pagination links
        model.addAttribute("lname", lname);
        model.addAttribute("instId", instId);

        return "results_studentwise_search";
    }

    /**
     * Displays the academic results for a specific student, mirroring the structure
     * of the academic results section in student_details.
     *
     * @param id The institution ID of the student.
     * @param model Model to add attributes for the Thymeleaf view.
     * @return The name of the Thymeleaf template for student results details, or an error page if student not found.
     */
    @GetMapping("/results/studentwise/{id}")
    public String showStudentResults(
            @PathVariable String id,
            Model model) {

        // Find the student by institution ID (instId)
        List<Student> students = studentRepository.findStudentByInstIdWithLatestRegistration(id);
        if (students.isEmpty()) {
            // If no student found, return a 404 error page
            return "error/404";
        }
        Student student = students.get(0); // Assuming findStudentByInstIdWithLatestRegistration returns a list, take the first one

        // === Fetch Registrations, Registered Courses, and Semester Results ===
        List<StudentRegistrations> registrations = studentRegistrationsRepository
                .findAllRegistrationsByStudentIdOrderBySemesterSequence(student.getStdid());

        Map<Long, List<Object[]>> regCourses = new LinkedHashMap<>();
        Map<Long, List<StudentSemesterResult>> regResults = new LinkedHashMap<>();

        for (StudentRegistrations reg : registrations) {
            regCourses.put(reg.getSrgid(), studentRegistrationCourseRepository
                    .findActiveRegistrationCourseDetails(reg.getSrgid()));
            regResults.put(reg.getSrgid(), studentSemesterResultRepository
                    .findByStudentRegistration_SrgidAndRowStateGreaterThan(reg.getSrgid(), (short) 0));
        }

        // === Egcrstt1 Result Grouping for Grade Mapping ===
        List<Egcrstt1> resultRecords = egcrstt1Repository.findAllById_StudId(student.getStdid());

        Map<Long, List<Object[]>> gradeExamMap = new LinkedHashMap<>();
        // Populate gradeExamMap based on student_details logic (tcrid -> [grad_lt, examtype_title])
        for (Egcrstt1 egcrstt1 : resultRecords) {
            // Assuming egcrstt1.getId().getTcrid() is the key to link to course[0]
            if (!gradeExamMap.containsKey(egcrstt1.getId().getTcrid())) {
                List<Object[]> gradeAndTitles = egcrstt1Repository.findGradeAndExamTitle(student.getStdid(), egcrstt1.getId().getTcrid());
                gradeExamMap.put(egcrstt1.getId().getTcrid(), gradeAndTitles);
            }
        }

        // Add attributes to the model for the Thymeleaf view
        model.addAttribute("student", student);
        model.addAttribute("registrations", registrations);
        model.addAttribute("regCourses", regCourses);
        model.addAttribute("regResults", regResults);
        model.addAttribute("gradeExamMap", gradeExamMap); // This map is crucial for displaying grades
        // model.addAttribute("gradeMap", gradeMap); // No longer directly needed as gradeExamMap contains what's needed
                                                  // unless grad_nm is directly pulled from gradeMap based on grad_id which isn't happening now.

        return "results_studentwise_details";
    }
}
