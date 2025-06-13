package com.segs.demo.controller.faculty;

import com.segs.demo.model.Course;
import com.segs.demo.repository.CourseRepository;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

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

}
