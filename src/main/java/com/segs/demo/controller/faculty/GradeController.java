package com.segs.demo.controller.faculty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.segs.demo.model.Grade;
import com.segs.demo.model.StudentGradeDTO;
import com.segs.demo.model.StudentGradeDTOWrapper;
import com.segs.demo.repository.TermCourseRepository;
import com.segs.demo.service.GradeService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/grades")
@SessionAttributes("gradeForm") // ADD THIS LINE
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private TermCourseRepository termCourseRepository;

    @GetMapping("/upload")
    public String showUploadForm(ModelMap model,HttpSession session) {
        // Get CRSID, TRMID, and EXAMTYPEID from session
        Long crsid = (Long) session.getAttribute("CRSID");
        Long trmid = (Long) session.getAttribute("TRMID");
        Long examTypeId = (Long) session.getAttribute("examTypeId");
        List<String> selectedGrades = List.of("AA", "AB", "BB", "BC", "CC", "CD", "DD", "F");
        // Fetch students and grades
        List<StudentGradeDTO> studentGrades = gradeService.getStudentGrades(crsid, trmid, examTypeId, selectedGrades);
        List<Grade> grades = gradeService.getAllGrades();
         StudentGradeDTOWrapper gradeForm = new StudentGradeDTOWrapper();
        if (studentGrades != null) {
            gradeForm.setGradesList(studentGrades);
        } else {
            gradeForm.setGradesList(new ArrayList<>());
        }

        // Add the session attributes to the model so they can be accessed in the view
        String termName = gradeService.getTermName(trmid);
        String courseName = gradeService.getCourseName(crsid);
        model.addAttribute("TermName", termName);
        model.addAttribute("CourseName", courseName);

        //model.addAttribute("studentGrades", studentGrades);
        model.addAttribute("grades", grades);
        model.addAttribute("gradeForm", gradeForm); // Optional

        return "upload-grades";
    }    

    @PostMapping("/upload")
    public String saveGrades(@ModelAttribute("gradeForm") StudentGradeDTOWrapper gradeWrapper,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        Long trmid = (Long) session.getAttribute("TRMID");
        Long crsid = (Long) session.getAttribute("CRSID");
        Long examTypeId = (Long) session.getAttribute("examTypeId");

        if (trmid == null || crsid == null || examTypeId == null) {
            redirectAttributes.addFlashAttribute("error", "Session expired or incomplete context.");
            return "redirect:/directGradeEntry/gradeOptions";
        }

        Long tcrid = termCourseRepository.findTcridByCrsidAndTrmid(crsid, trmid);
        if (tcrid == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid Term-Course combination.");
            return "redirect:/directGradeEntry/gradeOptions";
        }

        List<StudentGradeDTO> gradesToProcess = gradeWrapper.getGradesList().stream()
                                                    .filter(StudentGradeDTO::isSelectedForUpdate)
                                                    .collect(Collectors.toList());

        if (gradesToProcess.isEmpty()) {
            redirectAttributes.addFlashAttribute("warning", "No students were selected for grade update.");
            return "redirect:/directGradeEntry/gradeOptions";
        }
        
        gradeService.saveOrUpdateGrades(gradesToProcess, tcrid, examTypeId);
        redirectAttributes.addFlashAttribute("success", "Grades updated successfully!");

        return "redirect:/directGradeEntry/gradeOptions";
    }

}
