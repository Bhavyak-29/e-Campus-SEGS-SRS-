package com.ec2.main.controller.faculty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ec2.main.model.AcademicYears;
import com.ec2.main.model.Courses;
import com.ec2.main.model.Terms;
import com.ec2.main.model.TermCourses;
import com.ec2.main.repository.AcademicYearsRepository;
import com.ec2.main.repository.CoursesRepository;
import com.ec2.main.repository.TermCoursesRepository;
import com.ec2.main.repository.TermsRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private TermCoursesRepository termCoursesRepository;

    @Autowired
    private TermsRepository termsRepository;

    @Autowired
    private AcademicYearsRepository academicYearsRepository;


    // View: Display Master Course List
    @GetMapping("/master-list")
    public String showCourseMasterList(Model model) {
        List<Courses> courses = coursesRepository.findAll();
        model.addAttribute("courses", courses);
        return "course_list"; // Thymeleaf template under resources/templates/
    }

    @GetMapping("/excel")
    public void downloadCourseExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=courses_master_list.xlsx");

        List<Courses> courses = coursesRepository.findAll();

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
            for (Courses course : courses) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rowNum - 1);
                row.createCell(1).setCellValue(course.getCrsname());
                row.createCell(2).setCellValue(course.getCrstitle());
                row.createCell(3).setCellValue(course.getCrsdiscipline());
                row.createCell(4).setCellValue(course.getCrscode());
                row.createCell(5).setCellValue(course.getCrsassessmenttype());
                String ltp = String.format("%s-%s-%s (%s)",
                        course.getCrslectures(),
                        course.getCrstutorials(),
                        course.getCrspracticals(),
                        course.getCrscreditpoints());
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
    
    @GetMapping("/course-maintenance")
    public String showCourseListAdmin(Model model) {
        // Fetch all courses
        List<Courses> courses = coursesRepository.findAll();

        // Add the courses to the model for Thymeleaf
        model.addAttribute("courses", courses);

        // Return the Thymeleaf template for the admin course list
        return "courseList"; // Make sure this is the file name: src/main/resources/templates/courseList.html
    }
    @GetMapping("/course-edit")
    public String showEditCourseForm(Model model) {
        return "courseEdit"; // Thymeleaf template for editing
    }
    @GetMapping("/term")
    public String showTermCourseSelector(
            @RequestParam(value = "academicYearId", required = false) Long academicYearId,
            @RequestParam(value = "termId", required = false) Long termId,
            Model model,
            HttpSession session
    ) {
        model.addAttribute("academicYears", academicYearsRepository.findAll());

        if (academicYearId != null) {
            model.addAttribute("terms", termsRepository.findByAcademicYear_Ayrid(academicYearId));
            model.addAttribute("selectedAcademicYearId", academicYearId);
            model.addAttribute("termId", termId);
        }

        if (academicYearId != null && termId != null) {
            List<TermCourses> termCourses = termCoursesRepository.findByTerm_AcademicYear_AyridAndTerm_Trmid(academicYearId, termId);
            model.addAttribute("termCourses", termCourses);

            // optional: control checkboxes based on session userId
            Long userId = (Long) session.getAttribute("userid");
            model.addAttribute("showOverlap", userId != null && (userId == 7 || userId == 20 || userId == 1148));
        }

         if (academicYearId != null) {
            AcademicYears selectedYear = academicYearsRepository.findById(academicYearId).orElse(null);
            if (selectedYear != null)
                model.addAttribute("ayrname", selectedYear.getAyrname());
            
        }
        
        if (termId != null) {
            Terms selectedTerm = termsRepository.findById(termId).orElse(null);
            if (selectedTerm != null)
                model.addAttribute("trmname", selectedTerm.getTrmname());
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

        List<TermCourses> termCourses = termCoursesRepository.findByTerm_AcademicYear_AyridAndTerm_Trmid(academicYearId, termId);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Term Courses");
            Row header = sheet.createRow(0);
            String[] headers = {"Sr No.", "Course Name", "Course Credit", "Course Type", "Assigned Faculty"};
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }
            int rowNum = 1;
            for (TermCourses tc : termCourses) {
                Row row = sheet.createRow(rowNum++);

                int col = 0;
                row.createCell(col++).setCellValue(rowNum - 1); // Sr No
                row.createCell(col++).setCellValue(tc.getCourse().getCrsname());
                row.createCell(col++).setCellValue(tc.getCourse().getCrscreditpoints() != null ? tc.getCourse().getCrscreditpoints().doubleValue() : 0.0);
                row.createCell(col++).setCellValue(tc.getCourse().getCrscode() != null ? tc.getCourse().getCrscode() : ""); 
                row.createCell(col++).setCellValue(tc.getUser() != null ? tc.getUser().getUname() : "N/A");
                row.createCell(col++).setCellValue(tc.getUser() != null ? tc.getUser().getUemail() : "N/A");
                
                // If you want to include a "Check" column only conditionally, you can add:
                row.createCell(col++).setCellValue("✓");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(response.getOutputStream());
        }
    }

    // @GetMapping("/search")
    // public List<Courses> searchCourses(@RequestParam("query") String query) {
    //     return coursesRepository.findByCrsnameContainingIgnoreCaseOrCrscodeContainingIgnoreCase(query, query);
    // }

    @GetMapping("/search")
    @ResponseBody
    public List<Map<String, Object>> searchCourses(@RequestParam("q") String query) {
        List<Courses> courseList = coursesRepository.searchCourses(query);
        List<Map<String, Object>> results = new ArrayList<>();

        for (Courses c : courseList) {
            Map<String, Object> map = new HashMap<>();
            map.put("crsid", c.getCrsid());
            map.put("crscode", c.getCrscode());
            map.put("crsname", c.getCrsname());
            map.put("crscreditpoints", c.getCrscreditpoints());
            results.add(map);
        }
        return results;
        }
}
