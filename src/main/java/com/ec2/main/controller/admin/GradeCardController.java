package com.ec2.main.controller.admin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ec2.main.model.Semesters;
import com.ec2.main.model.StudentRegistration;
import com.ec2.main.model.StudentSemesterResult;
import com.ec2.main.model.Students;
import com.ec2.main.repository.SemestersRepository;
import com.ec2.main.repository.StudentRegistrationCoursesRepository;
import com.ec2.main.repository.StudentRegistrationRepository;
import com.ec2.main.repository.StudentSemesterResultRepository;
import com.ec2.main.repository.StudentsRepository;

@Controller
@RequestMapping("/results/gradecard")
public class GradeCardController {

    @Autowired
    private StudentSemesterResultRepository studentSemesterResultRepository;

    @Autowired
    private SemestersRepository semestersRepository;

    @Autowired
    private StudentsRepository studentsRepository;

    @Autowired
    private StudentRegistrationRepository studentRegistrationRepository;

    @Autowired
    private StudentRegistrationCoursesRepository studentRegistrationCoursesRepository;

    /**
     * Semester-wise: list of students for selected semester,
     * with link to individual grade cards.
     *
     * Called after submitting prgBchSemSelector with
     * target=/results/gradecard/semester/list
     */
    @GetMapping("/semester/list")
    public String showSemesterGradeCardList(Model model,
                                            HttpSession session,
                                            @RequestParam(name = "semesterId", required = false) String semesterIdParam,
                                            RedirectAttributes redirectAttributes) {

        Long semesterId;

        // 1. Get semesterId from request or session (same pattern as SPI/CPI)
        if (semesterIdParam != null && !semesterIdParam.isEmpty()) {
            try {
                semesterId = Long.parseLong(semesterIdParam);
                session.setAttribute("currentSemesterId", semesterId);
            } catch (NumberFormatException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid semester ID.");
                return "redirect:/results/spicpi/selector";
            }
        } else {
            Object sessionSemesterId = session.getAttribute("currentSemesterId");
            if (sessionSemesterId instanceof Long) {
                semesterId = (Long) sessionSemesterId;
            } else if (sessionSemesterId instanceof String && !((String) sessionSemesterId).isEmpty()) {
                try {
                    semesterId = Long.parseLong((String) sessionSemesterId);
                } catch (NumberFormatException e) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Invalid semester ID in session.");
                    return "redirect:/results/spicpi/selector";
                }
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Please select Program, Batch and Semester.");
                return "redirect:/results/spicpi/selector";
            }
        }

        // 2. Fetch list of students for this semester with SPI/CPI
        //    Reuse the same custom method as spicpi/list but without filters
        List<Object[]> results = studentSemesterResultRepository.findSpiCpiListBySpecification(
                semesterId,
                null, null, null,   // CPI filters
                null, null, null,   // SPI filters
                "and",
                "STDINSTID",
                "ASC"
        );

        Semesters sem = semestersRepository.findById(semesterId).orElse(null);
        model.addAttribute("selectedSemesterName", sem != null ? sem.getStrname() : "Selected Semester");
        model.addAttribute("semesterId", semesterId);
        model.addAttribute("studentSemesterResultsCollection", results);

        return "gradecard_semester_list"; // view 1 (list)
    }

    /**
     * Individual semester grade card for one student.
     * We show all registered courses and grades for that semester.
     */
    @GetMapping("/semester/{instId}")
    public String showSemesterGradeCardForStudent(@PathVariable("instId") String instId,
                                                  @RequestParam("semesterId") Long semesterId,
                                                  Model model) {

        // 1. Find student by institute ID
        List<Students> students = studentsRepository.findStudentByInstIdWithLatestRegistration(instId);
        if (students.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        }
        Students student = students.get(0);

        // 2. Find registration for this semester
        //    (you may already have a method similar to this; if not, add it in repository)
        StudentRegistration registration =
                studentRegistrationRepository.findByStudentIdAndSemesterId(student.getStdid(), semesterId);

        if (registration == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No registration found for this semester");
        }

        // 3. Fetch course details + grades for this registration
        //    You already use this in ResultController.showStudentResults()
        //    It typically returns Object[]: [courseCode, courseName, credits, grade, ...]
        List<Object[]> courseDetails =
                studentRegistrationCoursesRepository.findActiveRegistrationCourseDetails(registration.getSrgid());

        // 4. Put into a map or send raw list to the view
        Map<String, Object> header = new LinkedHashMap<>();
        header.put("studentInstId", student.getStdinstid());
        header.put("studentName", student.getStdfirstname() + " " + student.getStdlastname());

        Semesters sem = semestersRepository.findById(semesterId).orElse(null);
        header.put("semesterName", sem != null ? sem.getStrname() : "");

        // Optionally, you may also want SPI/CPI from StudentSemesterResult
        StudentSemesterResult ssr =
                studentSemesterResultRepository.findByStudentRegistration_SrgidAndRowStateGreaterThan(
                        registration.getSrgid(), (short) 0)
                        .stream().findFirst().orElse(null);

        header.put("SPI", ssr != null ? ssr.getSpi() : null);
        header.put("CPI", ssr != null ? ssr.getCpi() : null);

        model.addAttribute("header", header);
        model.addAttribute("courses", courseDetails);

        return "gradecard_semester_student"; // view 2 (single grade card)
    }
}
