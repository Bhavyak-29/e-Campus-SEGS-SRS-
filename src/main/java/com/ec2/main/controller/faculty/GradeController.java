package com.ec2.main.controller.faculty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ec2.main.model.Grade;
import com.ec2.main.model.StudentGradeDTO;
import com.ec2.main.model.StudentGradeDTOWrapper;
import com.ec2.main.repository.TermCoursesRepository;
import com.ec2.main.service.GradeService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/grades")
@SessionAttributes("gradeForm") // ADD THIS LINE
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private TermCoursesRepository termCourseRepository;

    @GetMapping("/upload")
    public String showUploadForm(ModelMap model,HttpSession session) {
        // Get CRSID, TRMID, and EXAMTYPEID from session
        Long crsid = (Long) session.getAttribute("CRSID");
        Long trmid = (Long) session.getAttribute("TRMID");
        Long examTypeId = (Long) session.getAttribute("examTypeId");
        List<String> selectedGrades = List.of("AA", "AB", "BB", "BC", "CC", "CD", "DD", "F","NULL");
        if (crsid == null || trmid == null || examTypeId == null) {
            model.addAttribute("error", "Session expired or missing data. Please reselect options.");
            return "redirect:/directGradeEntry";
        }
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
            return "redirect:/directGradeEntry";
        }

        Long tcrid = termCourseRepository.findTcridByCrsidAndTrmid(crsid, trmid);
        if (tcrid == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid Term-Course combination.");
            return "redirect:/directGradeEntry";
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


    @GetMapping("/update")
    public String showUpdateForm(ModelMap model,HttpSession session) {
        // Get CRSID, TRMID, and EXAMTYPEID from session
        Long crsid = (Long) session.getAttribute("CRSID");
        Long trmid = (Long) session.getAttribute("TRMID");
        Long examTypeId = (Long) session.getAttribute("examTypeId");
        List<String> selectedGrades = List.of("AA", "AB", "BB", "BC", "CC", "CD", "DD", "F");
        if (crsid == null || trmid == null || examTypeId == null) {
            model.addAttribute("error", "Session expired or missing data. Please reselect options.");
            return "redirect:/directGradeEntry";
        }
        // Fetch students and grades
        List<StudentGradeDTO> studentGrades = gradeService.getStudentGrades(crsid, trmid, examTypeId, selectedGrades);
       studentGrades = studentGrades.stream()
        .filter(dto -> dto.getGrade() != null && !dto.getGrade().equalsIgnoreCase("NULL"))
        .collect(Collectors.toList());
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

        return "update-grades";
    }    

    @PostMapping("/update")
    public String updateGrades(@ModelAttribute("gradeForm") StudentGradeDTOWrapper gradeWrapper,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        Long trmid = (Long) session.getAttribute("TRMID");
        Long crsid = (Long) session.getAttribute("CRSID");
        Long examTypeId = (Long) session.getAttribute("examTypeId");

        if (trmid == null || crsid == null || examTypeId == null) {
            redirectAttributes.addFlashAttribute("error", "Session expired or incomplete context.");
            return "redirect:/directGradeEntry";
        }

        Long tcrid = termCourseRepository.findTcridByCrsidAndTrmid(crsid, trmid);
        if (tcrid == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid Term-Course combination.");
            return "redirect:/directGradeEntry";
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


    @GetMapping("/chart-data")
    @ResponseBody
    public ResponseEntity<List<StudentGradeDTO>> getChartData(HttpSession session) {
        Long crsid = (Long) session.getAttribute("CRSID");
        Long trmid = (Long) session.getAttribute("TRMID");
        Long examTypeId = (Long) session.getAttribute("examTypeId");

        if (crsid == null || trmid == null || examTypeId == null) {
            return ResponseEntity.badRequest().build();
        }

        List<String> selectedGrades = List.of("AA", "AB", "BB", "BC", "CC", "CD", "DD", "F");

        List<StudentGradeDTO> studentGrades = gradeService.getStudentGrades(crsid, trmid, examTypeId, selectedGrades);

        return ResponseEntity.ok(studentGrades);
    }
    @GetMapping("/reviseIGrade")
    public String showReviseForm(ModelMap model,HttpSession session) {
        // Get CRSID, TRMID, and EXAMTYPEID from session
        Long crsid = (Long) session.getAttribute("CRSID");
        Long trmid = (Long) session.getAttribute("TRMID");
        Long examTypeId = (Long) session.getAttribute("examTypeId");
        if (crsid == null || trmid == null || examTypeId == null) {
            model.addAttribute("error", "Session expired or missing data. Please reselect options.");
            return "redirect:/directGradeEntry";
        }
        List<String> selectedGrades = List.of("I");
        // Fetch students and grades
        List<StudentGradeDTO> studentGrades = gradeService.getStudentGrades(crsid, trmid, examTypeId, selectedGrades);
        studentGrades = studentGrades.stream()
            .filter(dto -> dto.getGrade() != null && dto.getGrade().equalsIgnoreCase("I"))
            .collect(Collectors.toList());
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

        return "reviseIGrade";
    }    
    @PostMapping("/reviseIGrade")
    public String saveReviseGrade(@ModelAttribute("gradeForm") StudentGradeDTOWrapper gradeWrapper,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        Long trmid = (Long) session.getAttribute("TRMID");
        Long crsid = (Long) session.getAttribute("CRSID");
        Long examTypeId = (Long) session.getAttribute("examTypeId");

        if (trmid == null || crsid == null || examTypeId == null) {
            redirectAttributes.addFlashAttribute("error", "Session expired or incomplete context.");
            return "redirect:/directGradeEntry";
        }

        Long tcrid = termCourseRepository.findTcridByCrsidAndTrmid(crsid, trmid);
        if (tcrid == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid Term-Course combination.");
            return "redirect:/directGradeEntry";
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
