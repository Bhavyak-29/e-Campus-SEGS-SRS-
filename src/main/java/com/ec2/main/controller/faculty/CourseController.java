package com.ec2.main.controller.faculty;

import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private com.ec2.main.service.CourseService courseService;

    @Autowired
    private TermCoursesRepository termCoursesRepository;

    @Autowired
    private TermsRepository termsRepository;

    @Autowired
    private AcademicYearsRepository academicYearsRepository;


// View: Display Master Course List (with full-text search + pagination)
@GetMapping("/master-list")
public String showCourseMasterList(
        Model model,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String keyword
) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Courses> coursesPage;

    // If search keyword present, perform full-text search
    if (keyword != null && !keyword.trim().isEmpty()) {
        coursesPage = coursesRepository.searchCourses(keyword.trim(), pageable);
    } else {
        // Default: show all active courses (CRSROWSTATE > 0)
        coursesPage = coursesRepository.findByCrsrowstateGreaterThan(0, pageable);
    }

    model.addAttribute("coursesPage", coursesPage);
    model.addAttribute("currentPage", page);
    model.addAttribute("pageSize", size);
    model.addAttribute("totalPages", coursesPage.getTotalPages());
    model.addAttribute("keyword", keyword); // so search box retains text

    return "courseList"; // Thymeleaf template
}


    @GetMapping("/archived")
    public String showArchivedCourses(Model model) {
        List<Courses> archivedCourses = coursesRepository.findByCrsrowstate(0);
        model.addAttribute("archivedCourses", archivedCourses);
        return "courseArchived"; // New Thymeleaf template
    }

    @GetMapping("/restore/{id}")
    public String restoreArchivedCourse(@PathVariable("id") Long id) {
        courseService.restoreCourse(id); // sets CRSROWSTATE = 1
        return "redirect:/courses/archived";
    }
    @GetMapping("/archive/{id}")
public String archiveCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    courseService.archiveCourse(id);
    redirectAttributes.addFlashAttribute("message", "Course archived successfully!");
    return "redirect:/courses/master-list"; // go back to list page
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
    
   @GetMapping("/new")
    public String showNewCourseForm(Model model) {
        model.addAttribute("course", new Courses());
        return "courseNew"; // Thymeleaf template for adding/editing
    }

    @PostMapping("/save")
    public String saveCourse(@ModelAttribute("course") Courses course) {
        courseService.addCourse(course);
        return "redirect:/courses/master-list";
    }

@PostMapping("/update/{id}")
public String updateCourse(@PathVariable("id") Long id, @ModelAttribute("course") Courses updatedCourse) {
    courseService.updateCourse(id, updatedCourse);
    return "redirect:/courses/master-list"; // fixed
}

@GetMapping("/delete/{id}")
public String deleteCourse(@PathVariable("id") Long id) {
    courseService.deleteCourse(id);
    return "redirect:/courses/master-list"; // fixed
}


     // Show form to edit course
    @GetMapping("/edit/{id}")
    public String showEditCourseForm(@PathVariable("id") Long id, Model model) {
        Courses course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        return "courseNew"; // reuse same form
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
}
