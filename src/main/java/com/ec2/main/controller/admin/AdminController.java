package com.ec2.main.controller.admin;

import com.ec2.main.model.TermCourses;
import com.ec2.main.model.Users;
import com.ec2.main.repository.TermCoursesRepository;
import com.ec2.main.repository.UsersRepository;
import com.ec2.main.service.DefineElectivesService;
import com.ec2.main.service.facultyService;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;


@Controller
public class AdminController {

    @Autowired
    private DefineElectivesService defineElectivesService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TermCoursesRepository termCoursesRepository;

    @Autowired
    private facultyService facultyService;

    // ✅ Step 1: Show all courses for current term
    @GetMapping("/admin/assign-faculty")
    public String showAssignFacultyPage(HttpSession session, Model model) {
        Long latestTermId = (Long) session.getAttribute("latesttrmid");
        if (latestTermId == null) {
            latestTermId = 59L;
            session.setAttribute("latesttrmid", latestTermId);
        }

        Long prevTermId = latestTermId - 3;
        defineElectivesService.copyPreviousTermCoursesIfNeeded(prevTermId, latestTermId, session);

        List<TermCourses> termCourses = defineElectivesService.getTermCoursesForTerm(latestTermId);
        model.addAttribute("termCourses", termCourses);
        return "assign-faculty";
    }

       @GetMapping("/admin/faculty/master-list")
public String showFacultyMasterList(@RequestParam Long crsId,
                                    @RequestParam Integer slotNo,
                                    @RequestParam(required = false) String search,
                                    Model model) {

    List<Users> facultyList;

    // 🔍 Full-text search using Postgres (from UserRepository)
    if (search != null && !search.trim().isEmpty()) {
        facultyList = facultyService.searchFaculties(search.trim()); // uses to_tsvector() query
    } else {
        facultyList = facultyService.getAllFaculties(); // urole = 913, row_state > 0
    }

    model.addAttribute("facultyList", facultyList);
    model.addAttribute("crsId", crsId);
    model.addAttribute("slotNo", slotNo);
    model.addAttribute("search", search);
    return "faculty-master-list";
}
    // ✅ Step 3: Assign selected faculty to course
    @PostMapping("/admin/faculty/assign")
    public String assignFacultyToCourse(@RequestParam("facultyId") Long facultyId,
                                        @RequestParam("crsId") Long crsId,
                                        @RequestParam("slotNo") Integer slotNo,
                                        HttpSession session,
                                        RedirectAttributes redirectAttributes) {
        try {
            Long latestTermId = (Long) session.getAttribute("latesttrmid");
            if (latestTermId == null) latestTermId = 59L;

            // ✅ Find term course for this course in current term
            Long tcrId = termCoursesRepository.findTcrid(crsId, latestTermId);
            if (tcrId == null) {
                redirectAttributes.addFlashAttribute("error", "⚠️ No term-course found for this course!");
                return "redirect:/admin/assign-faculty";
            }

            TermCourses tc = termCoursesRepository.findById(tcrId).orElse(null);
            if (tc == null) {
                redirectAttributes.addFlashAttribute("error", "⚠️ TermCourse record missing!");
                return "redirect:/admin/assign-faculty";
            }

            // ✅ Update faculty & slot
            tc.setTcrfacultyid(facultyId);
            tc.setTcrslot(slotNo);
            tc.setTcrlastupdatedat(LocalDateTime.now());
            tc.setTcrlastupdatedby(3809L);

            termCoursesRepository.save(tc);

            // ✅ Fetch readable info for toast message
            Users faculty = usersRepository.findById(facultyId).orElse(null);
            String facultyName = faculty != null
                    ? (faculty.getUfullname() != null ? faculty.getUfullname() : faculty.getUemail())
                    : "Faculty ID " + facultyId;

            redirectAttributes.addFlashAttribute("success",
                    "✅ Assigned " + facultyName + " to Course ID " + crsId + " (Slot " + slotNo + ").");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "❌ Error assigning faculty: " + e.getMessage());
        }

        // ✅ Redirect back to main page (with toast)
        return "redirect:/admin/assign-faculty";
    }

    @GetMapping("/admin/faculty-summary")
public String showFacultySummary(HttpSession session, Model model) {
    Long latestTermId = (Long) session.getAttribute("latesttrmid");
    if (latestTermId == null) latestTermId = 59L;

    List<Object[]> summaryList = termCoursesRepository.getFacultyAssignmentSummaryForTerm(latestTermId);

    List<Map<String, Object>> facultySummary = new ArrayList<>();
    for (Object[] obj : summaryList) {
        Map<String, Object> map = new HashMap<>();
        map.put("facultyId", obj[0]);
        map.put("facultyEmail", obj[1]);
        map.put("assignedCount", obj[2]);
        facultySummary.add(map);
    }

    model.addAttribute("facultySummary", facultySummary);
    return "faculty-summary";
}
    @GetMapping("/admin/faculty/{facultyId}/courses")
public String viewFacultyCourses(@PathVariable Long facultyId,
                                 HttpSession session,
                                 Model model) {
    Long latestTermId = (Long) session.getAttribute("latesttrmid");
    if (latestTermId == null) latestTermId = 59L;

    List<Object[]> list = termCoursesRepository.getCoursesForFacultyInTerm(facultyId, latestTermId);

    List<Map<String, Object>> facultyCourses = new ArrayList<>();
    for (Object[] row : list) {
        Map<String, Object> m = new HashMap<>();
        m.put("crsId", row[0]);
        m.put("crsName", row[1] != null ? row[1] : row[2]);
        m.put("slotNo", row[3]);
        facultyCourses.add(m);
    }

    // Fetch faculty email
    String facultyEmail = usersRepository.findById(facultyId)
            .map(Users::getUemail)
            .orElse("Unknown");

    model.addAttribute("facultyCourses", facultyCourses);
    model.addAttribute("facultyId", facultyId);
    model.addAttribute("facultyEmail", facultyEmail);
    return "faculty-courses";
}
}