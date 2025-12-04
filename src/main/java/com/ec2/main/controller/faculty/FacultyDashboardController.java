package com.ec2.main.controller.faculty;

import com.ec2.main.model.TermCourses;
import com.ec2.main.model.Terms;
import com.ec2.main.model.Users;
import com.ec2.main.repository.TermCoursesRepository;
import com.ec2.main.repository.TermsRepository;
import com.ec2.main.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/faculty")
public class FacultyDashboardController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TermsRepository termsRepository;

    @Autowired
    private TermCoursesRepository termCoursesRepository;

    @GetMapping("/current-courses")
    public String showCurrentSemesterCourses(Model model, HttpSession session) {

        // 👉 for FACULTY, username == univId (e.g. 200491003)
        String univId = (String) session.getAttribute("username");
        System.out.println("FacultyDashboard: session.username = " + univId);

        if (univId == null || univId.isBlank()) {
            return "redirect:/auth/login";
        }

        // 1️⃣ Load Users row by univId
        Users faculty = userRepository.findByUnivId(univId).orElse(null);
        System.out.println("FacultyDashboard: faculty from DB = " +
                (faculty != null ? faculty.getUnivId() : "null"));

        if (faculty == null || faculty.getUrole0() == null
                || !faculty.getUrole0().equalsIgnoreCase("FACULTY")) {
            model.addAttribute("faculty", null);
            model.addAttribute("latestTerm", null);
            model.addAttribute("termCourses", List.of());
            model.addAttribute("errorMessage", "Faculty user not found or not active.");
            return "segsMenuFaculty";
        }

        // 2️⃣ Latest active term
        Terms latestTerm = termsRepository
                .findLatestMinusThree(0);

        if (latestTerm == null) {
            model.addAttribute("faculty", faculty);
            model.addAttribute("latestTerm", null);
            model.addAttribute("termCourses", List.of());
            model.addAttribute("errorMessage", "No active term found.");
            return "segsMenuFaculty";
        }

        Long latestTermId = latestTerm.getTrmid();

        // 3️⃣ tcrfacultyid stores numeric univId
        Long facultyKey = faculty.getUid();  // assuming getUid() returns Long
        System.out.println("FacultyDashboard: latestTermId = " + latestTermId +
                ", facultyKey = " + facultyKey);

        List<TermCourses> termCourses = termCoursesRepository.findFacultyCoursesInTerm(56L, facultyKey);


        System.out.println("FacultyDashboard: termCourses count = " + termCourses.size());

        // 4️⃣ Build student count map
        Map<Long, Long> studentCounts = new HashMap<>();

        if (!termCourses.isEmpty()) {
            List<Long> tcrIds = new ArrayList<>();
            for (TermCourses tc : termCourses) {
                tcrIds.add(tc.getTcrid());
            }

            List<Object[]> rows =
                    termCoursesRepository.countStudentsForTermCourses(tcrIds);

            // rows -> [tcrid, count]
            for (Object[] row : rows) {
                Long tcrid = ((Number) row[0]).longValue();
                Long count = ((Number) row[1]).longValue();
                studentCounts.put(tcrid, count);
            }
        }

        // 5️⃣ Send to view
        model.addAttribute("faculty", faculty);
        model.addAttribute("latestTerm", latestTerm);
        model.addAttribute("termCourses", termCourses);
        model.addAttribute("studentCounts", studentCounts);

        return "segsMenuFaculty";
    }
}
