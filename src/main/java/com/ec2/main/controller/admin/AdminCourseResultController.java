package com.ec2.main.controller.admin;

import com.ec2.main.model.AcademicYears;
import com.ec2.main.model.Courses;
import com.ec2.main.model.Egcrstt1;
import com.ec2.main.model.Grade;
import com.ec2.main.model.StudentGradeDTO;
import com.ec2.main.model.Terms;
import com.ec2.main.service.facultyService;
import com.ec2.main.service.GradeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/admin/course-results")
public class AdminCourseResultController {

    @Autowired
    private facultyService facultyService;

    @Autowired
    private GradeService gradeService;

    // 1️⃣ Selector page – same inputs as directGradeEntry (AY, Term, Course, ExamType)
    @GetMapping("/selector")
    public String showCourseResultSelector(ModelMap model, HttpSession session) {

        // (Optional) restrict to super-admin 99001
        Long userId = (Long) session.getAttribute("userid");
        if (userId == null || userId != 99001L) {
            return "redirect:/accessDenied";
        }

        List<AcademicYears> academicYears = facultyService.getAllAcademicYears();
        List<Terms> terms = facultyService.getAllTerms();
        List<Courses> courses = facultyService.getAllCourses();
        List<Egcrstt1> examTypes = facultyService.getAllExamTypes();

        // Same logic as directGradeEntry: pick latest AY + latest term in that AY
        AcademicYears latestAY = academicYears.stream()
                .max(Comparator.comparingLong(AcademicYears::getAyrid))
                .orElse(null);

        Terms latestTerm = null;
        if (latestAY != null) {
            latestTerm = terms.stream()
                    .filter(t -> t.getAcademicYear().getAyrid().equals(latestAY.getAyrid()))
                    .max(Comparator.comparingLong(Terms::getTrmid))
                    .orElse(null);
        }

        model.addAttribute("academicYears", academicYears);
        model.addAttribute("terms", terms);
        model.addAttribute("courses", courses);
        model.addAttribute("examTypes", examTypes);

        model.addAttribute("latestAY", latestAY != null ? latestAY.getAyrid() : null);
        model.addAttribute("latestTerm", latestTerm != null ? latestTerm.getTrmid() : null);

        return "admin_course_grade_selector";  // new Thymeleaf page
    }

    // 2️⃣ Grade list page – show grades for selected AY + Term + Course + ExamType
    @GetMapping("/list")
    public String showCourseGradeList(@RequestParam("AYRID") Long ayrid,
                                      @RequestParam("TRMID") Long trmid,
                                      @RequestParam("CRSID") Long crsid,
                                      @RequestParam("examTypeId") Long examTypeId,
                                      ModelMap model,
                                      HttpSession session) {

        Long userId = (Long) session.getAttribute("userid");
        if (userId == null || userId != 99001L) {
            return "redirect:/accessDenied";
        }

        // We want *all* grades, including NULL/I if you want:
        List<String> selectedGrades = List.of("AA", "AB", "BB", "BC", "CC", "CD", "DD", "F", "I", "NULL");

        List<StudentGradeDTO> studentGrades =
                gradeService.getStudentGrades(crsid, trmid, examTypeId, selectedGrades);

        // Optional: filter out null/NULL if you don't want them:
        // studentGrades = studentGrades.stream()
        //        .filter(dto -> dto.getGrade() != null && !dto.getGrade().equalsIgnoreCase("NULL"))
        //        .toList();

        // For header display
        String termName = gradeService.getTermName(trmid);
        String courseName = gradeService.getCourseName(crsid);

        model.addAttribute("AYRID", ayrid);
        model.addAttribute("TRMID", trmid);
        model.addAttribute("CRSID", crsid);
        model.addAttribute("examTypeId", examTypeId);

        model.addAttribute("termName", termName);
        model.addAttribute("courseName", courseName);
        model.addAttribute("studentGrades", studentGrades);

        return "admin_course_grade_list";   // new Thymeleaf page
    }
}
