package com.segs.demo.controller.faculty;

import com.segs.demo.model.AcademicYear;
import com.segs.demo.model.Course;
import com.segs.demo.model.Term;
import com.segs.demo.model.TermCourse;
import com.segs.demo.repository.AcademicYearRepository;
import com.segs.demo.repository.CourseRepository;
import com.segs.demo.repository.TermCourseRepository;
import com.segs.demo.repository.TermRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TermCourseRepository termCourseRepository;

    @Autowired
    private TermRepository termRepository;

    @Autowired
    private AcademicYearRepository academicYearRepository;


    // View: Display Master Course List
    @GetMapping("/master-list")
    public String showCourseMasterList(Model model) {
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        return "course_list"; // Thymeleaf template under resources/templates/
    }

    @GetMapping("/excel")
    public void downloadCourseExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=courses_master_list.xlsx");

        List<Course> courses = courseRepository.findAll();

        // Create Excel workbook and sheet
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Courses");

            // Header row
            Row header = sheet.createRow(0);
            String[] headers = {"Sr. No.", "Course Name", "Title", "Level", "Code", "Assessment Type", "L-T-P (C)"};
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }

            // Data rows
            int rowNum = 1;
            for (Course course : courses) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rowNum - 1);
                row.createCell(1).setCellValue(course.getName());
                row.createCell(2).setCellValue(course.getTitle());
                row.createCell(3).setCellValue(course.getDiscipline());
                row.createCell(4).setCellValue(course.getCode());
                row.createCell(5).setCellValue(course.getAssessmentType());
                String ltp = String.format("%s-%s-%s (%s)",
                        course.getLectures(),
                        course.getTutorials(),
                        course.getPracticals(),
                        course.getCreditPoints());
                row.createCell(6).setCellValue(ltp);
            }

            // Autosize columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to response stream
            workbook.write(response.getOutputStream());
        }
    }

    @GetMapping("/term")
    public String showTermCourseSelector(
            @RequestParam(value = "academicYearId", required = false) Long academicYearId,
            @RequestParam(value = "termId", required = false) Long termId,
            Model model,
            HttpSession session
    ) {
        model.addAttribute("academicYears", academicYearRepository.findAll());

        if (academicYearId != null) {
            model.addAttribute("terms", termRepository.findByAcademicYear_Id(academicYearId));
            model.addAttribute("selectedAcademicYearId", academicYearId);
            model.addAttribute("termId", termId);
        }

        if (academicYearId != null && termId != null) {
            List<TermCourse> termCourses = termCourseRepository.findByTerm_AcademicYear_IdAndTerm_Id(academicYearId, termId);
            model.addAttribute("termCourses", termCourses);

            // optional: control checkboxes based on session userId
            Integer userId = (Integer) session.getAttribute("userid");
            model.addAttribute("showOverlap", userId != null && (userId == 7 || userId == 20 || userId == 1148));
        }

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

        return "term_courses"; // Thymeleaf view
    }

    // STEP 2: Excel download
    @GetMapping("/term/excel")
    public void downloadTermCourseExcel(
            @RequestParam Long academicYearId,
            @RequestParam Long termId,
            HttpServletResponse response
    ) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=term_courses.xlsx");

        List<TermCourse> termCourses = termCourseRepository.findByTerm_AcademicYear_IdAndTerm_Id(academicYearId, termId);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Term Courses");
            Row header = sheet.createRow(0);
            String[] headers = {"Sr No.", "Course Name", "Course Credit", "Course Type", "Assigned Faculty"};
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }
            int rowNum = 1;
            for (TermCourse tc : termCourses) {
                Row row = sheet.createRow(rowNum++);

                int col = 0;
                row.createCell(col++).setCellValue(rowNum - 1); // Sr No
                row.createCell(col++).setCellValue(tc.getCourse().getName());
                row.createCell(col++).setCellValue(tc.getCourse().getCreditPoints() != null ? tc.getCourse().getCreditPoints().doubleValue() : 0.0);
                row.createCell(col++).setCellValue(tc.getCourse().getCode() != null ? tc.getCourse().getCode() : ""); 
                row.createCell(col++).setCellValue(tc.getUser() != null ? tc.getUser().getUserName() : "N/A");
                row.createCell(col++).setCellValue(tc.getUser() != null ? tc.getUser().getUserMailId() : "N/A");
                
                // If you want to include a "Check" column only conditionally, you can add:
                row.createCell(col++).setCellValue("✓");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(response.getOutputStream());
        }
    }
}
