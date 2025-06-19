package com.segs.demo.controller.faculty;

import com.segs.demo.model.*; // Assuming AcademicYear, Term, Course, TermCourse, ExamType are here

import com.segs.demo.repository.*; // Assuming new repositories are in this package

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Apache POI for Excel export
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


@Controller
@RequestMapping("/results") // Base path for all result-related functionalities
public class ResultController {

    // Repositories (Existing)
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private Egcrstt1Repository egcrstt1Repository;
    @Autowired
    private Eggradm1Repository eggradm1Repository;
    @Autowired
    private StudentRegistrationsRepository studentRegistrationsRepository;
    @Autowired
    private StudentRegistrationCourseRepository studentRegistrationCourseRepository;
    @Autowired
    private StudentSemesterResultRepository studentSemesterResultRepository;
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private BatchRepository batchRepository;
    @Autowired
    private SemesterRepository semesterRepository;

    // New Repositories for CourseWise -> Exam Specific
    @Autowired
    private AcademicYearRepository academicYearRepository; // Assuming this is your AcademicYear entity repository
    @Autowired
    private TermRepository termRepository;               // Assuming this is your Term entity repository
    @Autowired
    private TermCourseRepository termCourseRepository;   // Assuming this is your TermCourse entity repository
    @Autowired
    private ExamTypeRepository examTypeRepository;       // Assuming this is your ExamType entity (for EGEXAMM1) repository


    // --- Existing Student-wise Result Methods (No Changes) ---
    @GetMapping("/studentwise/search")
    public String searchStudentsForResults(
            @RequestParam(required = false) String fname,
            @RequestParam(required = false) String lname,
            @RequestParam(required = false) String instId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            ModelMap model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Student> studentsPage;

        if (instId != null && !instId.isEmpty()) {
            studentsPage = studentRepository.findByStdinstidAndStdrowstateGreaterThan(instId, (short) 0, pageable);
        } else if (fname != null && !fname.isEmpty() && lname != null && !lname.isEmpty()) {
            studentsPage = studentRepository.findByStdfirstnameContainingIgnoreCaseAndStdlastnameContainingIgnoreCaseAndStdrowstateGreaterThan(fname, lname, (short) 0, pageable);
        } else if (fname != null && !fname.isEmpty()) {
            studentsPage = studentRepository.findByStdfirstnameContainingIgnoreCaseAndStdrowstateGreaterThan(fname, (short) 0, pageable);
        } else if (lname != null && !lname.isEmpty()) {
            studentsPage = studentRepository.findByStdlastnameContainingIgnoreCaseAndStdrowstateGreaterThan(lname, (short) 0, pageable);
        } else {
            studentsPage = Page.empty(pageable);
        }

        model.addAttribute("studentsPage", studentsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentsPage.getTotalPages());
        model.addAttribute("totalItems", studentsPage.getTotalElements());
        model.addAttribute("fname", fname);
        model.addAttribute("lname", lname);
        model.addAttribute("instId", instId);

        return "results_studentwise_search";
    }

    @GetMapping("/studentwise/{id}")
    public String showStudentResults(
            @PathVariable String id,
            Model model) {

        List<Student> students = studentRepository.findStudentByInstIdWithLatestRegistration(id);
        if (students.isEmpty()) {
            return "error/404";
        }
        Student student = students.get(0);

        List<StudentRegistrations> registrations = studentRegistrationsRepository
                .findAllRegistrationsByStudentIdOrderBySemesterSequence(student.getStdid());

        Map<Long, List<Object[]>> regCourses = new LinkedHashMap<>();
        Map<Long, List<StudentSemesterResult>> regResults = new LinkedHashMap<>();

        for (StudentRegistrations reg : registrations) {
            regCourses.put(reg.getSrgid(), studentRegistrationCourseRepository
                    .findActiveRegistrationCourseDetails(reg.getSrgid()));
            regResults.put(reg.getSrgid(), studentSemesterResultRepository
                    .findByStudentRegistration_SrgidAndRowStateGreaterThan(reg.getSrgid(), (short) 0));
        }

        List<Egcrstt1> resultRecords = egcrstt1Repository.findAllById_StudId(student.getStdid());

        Map<Long, List<Object[]>> gradeExamMap = new LinkedHashMap<>();
        for (Egcrstt1 egcrstt1 : resultRecords) {
            if (!gradeExamMap.containsKey(egcrstt1.getId().getTcrid())) {
                List<Object[]> gradeAndTitles = egcrstt1Repository.findGradeAndExamTitle(student.getStdid(), egcrstt1.getId().getTcrid());
                gradeExamMap.put(egcrstt1.getId().getTcrid(), gradeAndTitles);
            }
        }

        model.addAttribute("student", student);
        model.addAttribute("registrations", registrations);
        model.addAttribute("regCourses", regCourses);
        model.addAttribute("regResults", regResults);
        model.addAttribute("gradeExamMap", gradeExamMap);

        return "results_studentwise_details";
    }


    // --- SPI-CPI List Functionality (Existing) ---

    /**
     * Displays the Program, Batch, and Semester selection page for SPI-CPI list.
     */
    @GetMapping("/spicpi/selector")
    public String showPrgBchSemSelector(Model model, HttpSession session,
                                        @RequestParam(name = "actionText", required = false) String actionText,
                                        @RequestParam(name = "target", required = false, defaultValue = "/results/spicpi/list") String targetUrl) {

        // Store selected action and target URL in session for persistence across requests
        session.setAttribute("actionText", actionText != null ? actionText : "Go to List");
        session.setAttribute("targetUrl", targetUrl);

        // Fetch initial data for dropdowns (all active programs, batches, semesters)
        List<Program> programs = programRepository.findByPrgrowstateGreaterThanOrderByPrgfield1Asc((short) 0);
        List<Batch> batches = batchRepository.findAllActiveBatchesOrderedByProgramAndBatchField();
        List<Semester> semesters = semesterRepository.findAllActiveSemestersOrderedByProgramBatchAndSequence();

        model.addAttribute("programs", programs);
        model.addAttribute("batches", batches);
        model.addAttribute("semesters", semesters);
        model.addAttribute("actionText", session.getAttribute("actionText")); // For the button text
        model.addAttribute("targetUrl", session.getAttribute("targetUrl")); // For form action

        return "prgBchSemSelector"; // Thymeleaf template name
    }

    /**
     * AJAX endpoint to get batches based on selected program.
     */
    @GetMapping("/spicpi/api/batches")
    @ResponseBody
    public List<Batch> getBatchesByProgram(@RequestParam Long programId) {
        return batchRepository.findByProgram_PrgidAndBchrowstateGreaterThanOrderByBchfield1Asc(programId, (short) 0);
    }

    /**
     * AJAX endpoint to get semesters based on selected batch.
     */
    @GetMapping("/spicpi/api/semesters")
    @ResponseBody
    public List<Semester> getSemestersByBatch(@RequestParam Long batchId) {
        return semesterRepository.findByBatch_BchidAndStrrowstateGreaterThanOrderByStrseqnoAsc(batchId, (short) 0);
    }


    /**
     * Displays the SPI-CPI list based on selected semester and filter criteria.
     * This method now leverages the custom repository implementation.
     */
    @GetMapping("/spicpi/list")
    public String showSpiCpiList(Model model, HttpSession session, HttpServletResponse response,
                                 @RequestParam(name = "semesterId", required = false) String semesterIdParam,
                                 @RequestParam(name = "operator", required = false) String cpiOperator,
                                 @RequestParam(name = "fromValue", required = false) BigDecimal cpiFromValue,
                                 @RequestParam(name = "toValue", required = false) BigDecimal cpiToValue,
                                 @RequestParam(name = "spiOperator", required = false) String spiOperator,
                                 @RequestParam(name = "spiFromValue", required = false) BigDecimal spiFromValue,
                                 @RequestParam(name = "spiToValue", required = false) BigDecimal spiToValue,
                                 @RequestParam(name = "condition", defaultValue = "and") String condition, // "and" or "or"
                                 @RequestParam(name = "orderBy", defaultValue = "STDINSTID") String orderBy,
                                 @RequestParam(name = "orderType", defaultValue = "ASC") String orderType,
                                 @RequestParam(name = "generateExcelSheet", required = false) String excelFlag, // Simpler name
                                 RedirectAttributes redirectAttributes) throws IOException {


        Long semesterId; // Declare semesterId as Long

        // 1. Try to get it from the request parameter first
        if (semesterIdParam != null && !semesterIdParam.isEmpty()) {
            try {
                semesterId = Long.parseLong(semesterIdParam);
                session.setAttribute("currentSemesterId", semesterId); // Store as Long in session
            } catch (NumberFormatException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid semester ID format.");
                return "redirect:/results/spicpi/selector";
            }
        } else {
            // 2. If not in request param, try to get it from session
            Object sessionSemesterId = session.getAttribute("currentSemesterId");
            if (sessionSemesterId instanceof Long) {
                semesterId = (Long) sessionSemesterId;
            } else if (sessionSemesterId instanceof String && !((String) sessionSemesterId).isEmpty()) {
                    try {
                    semesterId = Long.parseLong((String) sessionSemesterId);
                    } catch (NumberFormatException e) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Invalid semester ID format in session.");
                    return "redirect:/results/spicpi/selector";
                    }
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Please select a semester before viewing the list.");
                return "redirect:/results/spicpi/selector";
            }
        }

        if (semesterId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select a semester before viewing the list.");
            return "redirect:/results/spicpi/selector";
        }

        // Call the custom repository method to get the filtered and ordered data
        List<Object[]> results = studentSemesterResultRepository.findSpiCpiListBySpecification(
            semesterId,
            cpiOperator, cpiFromValue, cpiToValue,
            spiOperator, spiFromValue, spiToValue,
            condition,
            orderBy, orderType
        );

        if ("True".equalsIgnoreCase(excelFlag)) {
            return generateSpiCpiExcel(results, response);
        } else {
            model.addAttribute("studentSemesterResultsCollection", results); // Simpler attribute name
            model.addAttribute("cpiOperator", cpiOperator);
            model.addAttribute("cpiFromValue", cpiFromValue);
            model.addAttribute("cpiToValue", cpiToValue);
            model.addAttribute("spiOperator", spiOperator);
            model.addAttribute("spiFromValue", spiFromValue);
            model.addAttribute("spiToValue", spiToValue);
            model.addAttribute("condition", condition);
            model.addAttribute("orderBy", orderBy);
            model.addAttribute("orderType", orderType);

            return "spiCpiList"; // Thymeleaf template name
        }
    }


    /**
     * Generates an Excel sheet from the SPI-CPI list data.
     */
    private String generateSpiCpiExcel(List<Object[]> data, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=spi_cpi_list_" + System.currentTimeMillis() + ".xlsx";
        response.setHeader(headerKey, headerValue);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("SPI-CPI List");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Sr No");
        headerRow.createCell(1).setCellValue("Student Id");
        headerRow.createCell(2).setCellValue("Student Name");
        headerRow.createCell(3).setCellValue("SPI");
        headerRow.createCell(4).setCellValue("CPI");

        int rowNum = 1;

        for (Object[] rowData : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(rowNum - 1); // Sr No
            row.createCell(1).setCellValue(rowData[0] != null ? rowData[0].toString() : ""); // Student Id
            row.createCell(2).setCellValue(rowData[1] != null ? rowData[1].toString() : ""); // Student Name

            // Handle BigDecimal for SPI/CPI
            if (rowData[2] instanceof BigDecimal) {
                row.createCell(3).setCellValue(((BigDecimal) rowData[2]).doubleValue()); // SPI
            } else if (rowData[2] != null) {
                // Attempt to parse if it's a string, or just use toString
                try {
                    row.createCell(3).setCellValue(new BigDecimal(rowData[2].toString()).doubleValue());
                } catch (NumberFormatException e) {
                    row.createCell(3).setCellValue(rowData[2].toString());
                }
            }

            if (rowData[3] instanceof BigDecimal) {
                row.createCell(4).setCellValue(((BigDecimal) rowData[3]).doubleValue()); // CPI
            } else if (rowData[3] != null) {
                 try {
                    row.createCell(4).setCellValue(new BigDecimal(rowData[3].toString()).doubleValue());
                } catch (NumberFormatException e) {
                    row.createCell(4).setCellValue(rowData[3].toString());
                }
            }
        }

        workbook.write(response.getOutputStream());
        workbook.close();

        return null; // No view name needed as content is written directly to response
    }

    // --- NEW: Coursewise -> Exam Specific Functionality ---

    /**
     * Displays the Academic Year, Term, Course, Exam Type selection page.
     * Corresponds to SegsSubmittedTermCourseExamSelector servlet.
     */
    @GetMapping("/coursewise/exam-specific/selector")
    public String showCourseExamSelector(Model model, HttpSession session,
                                         @RequestParam(name = "actiontext", required = false) String actionText,
                                         @RequestParam(name = "target", required = false, defaultValue = "/results/coursewise/exam-specific/list") String targetUrl,
                                         @RequestParam(name = "fromWhichSystem", required = false) String fromWhichSystem) {

        // Store session attributes from the original servlet logic
        if (fromWhichSystem != null) {
            session.setAttribute("fromWhichSystem", fromWhichSystem);
        } else {
            fromWhichSystem = (String) session.getAttribute("fromWhichSystem");
        }

        if (actionText != null) {
            session.setAttribute("actionText", actionText);
        } else {
            actionText = (String) session.getAttribute("actionText");
        }

        if (targetUrl != null) {
            session.setAttribute("target", targetUrl);
        } else {
            targetUrl = (String) session.getAttribute("target");
        }

        // Fetch initial data for Academic Year dropdown
        List<AcademicYear> academicYears = academicYearRepository.findByRowStateGreaterThanOrderByField1Asc((int) 0);

        // Convert to List<DropdownItem> to match the JSP's expected format (id^name)
        List<DropdownItem> academicYearDropdown = academicYears.stream()
            .map(ay -> new DropdownItem(String.valueOf(ay.getId()), ay.getName()))
            .collect(Collectors.toList());

        model.addAttribute("academicYears", academicYearDropdown);
        model.addAttribute("actionText", actionText); // For display on JSP
        model.addAttribute("targetUrl", targetUrl);   // For form action on JSP

        // The other dropdowns (Term, Course, Exam Type) will be populated via AJAX
        return "courseExamSelector"; // Thymeleaf template for selection
    }

    /**
     * AJAX endpoint to get terms based on selected academic year.
     */
    @GetMapping("/coursewise/exam-specific/api/terms")
    @ResponseBody
    public List<DropdownItem> getTermsByAcademicYear(@RequestParam Long academicYearId) {
        List<Term> terms = termRepository.findByAcademicYear_IdAndRowStateGreaterThanOrderByField1Asc(academicYearId, (int) 0);
        return terms.stream()
            .map(term -> new DropdownItem(String.valueOf(term.getId()), term.getName()))
            .collect(Collectors.toList());
    }

    /**
     * AJAX endpoint to get courses based on selected term.
     * This specifically fetches courses that have 'T' (submitted) grades.
     */
    @GetMapping("/coursewise/exam-specific/api/courses")
    @ResponseBody
    public List<DropdownItem> getCoursesByTerm(@RequestParam Long termId) {
        return termCourseRepository.findSubmittedTermCoursesByTermId(termId);
    }

    /**
     * AJAX endpoint to get exam types based on selected course (TermCourseId).
     * This specifically fetches exam types for which grades exist for the given course.
     */
    @GetMapping("/coursewise/exam-specific/api/examtypes")
    @ResponseBody
    public List<DropdownItem> getExamTypesByCourse(@RequestParam Long termCourseId) {
        return examTypeRepository.findExamTypesWithGradesByTermCourseId(termCourseId);
    }

    /**
     * Displays the grade list report based on selected Academic Year, Term, Course, and Exam Type.
     * Corresponds to GradeListServlet and defaultGradeListReport.jsp.
     */
    @GetMapping("/coursewise/exam-specific/list")
    public String showGradeListReport(Model model, HttpSession session, HttpServletResponse response,
                                      @RequestParam(name = "academicYearId", required = false) Long academicYearId,
                                      @RequestParam(name = "termId", required = false) Long termId,
                                      @RequestParam(name = "termCourseId", required = false) Long termCourseId,
                                      @RequestParam(name = "examTypeId", required = false) Long examTypeId,
                                      @RequestParam(name = "grade", required = false) String[] selectedGrades, // For multi-select filter
                                      @RequestParam(name = "generateExcelSheet", required = false) String generateExcelSheet,
                                      RedirectAttributes redirectAttributes) throws IOException {

        // Retrieve from session if not present in request (mimics old servlet behavior)
        academicYearId = (academicYearId != null) ? academicYearId : (Long) session.getAttribute("academicYearId");
        termId = (termId != null) ? termId : (Long) session.getAttribute("termId");
        termCourseId = (termCourseId != null) ? termCourseId : (Long) session.getAttribute("termCourseId");
        examTypeId = (examTypeId != null) ? examTypeId : (Long) session.getAttribute("examTypeId");

        // Store in session for subsequent requests (e.g., Excel export, Back button)
        session.setAttribute("academicYearId", academicYearId);
        session.setAttribute("termId", termId);
        session.setAttribute("termCourseId", termCourseId);
        session.setAttribute("examTypeId", examTypeId);

        // Basic validation (mimics sumbitIt() from JSP)
        if (academicYearId == null || termId == null || termCourseId == null || examTypeId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select all required fields (Academic Year, Term Name, Course Name, Exam Type).");
            return "redirect:/results/coursewise/exam-specific/selector";
        }

        // Check if grade is computed/submitted (mimics !lSegsTermCourseGradingBn.getIsNotSubmittedFlag())
        boolean isGradeSubmitted = egcrstt1Repository.existsById_TcridAndId_ExamtypeIdAndRowStatusGreaterThan(termCourseId, examTypeId, "0");

        if (!isGradeSubmitted) {
            model.addAttribute("errorMessage", "Grade is not computed for the course.");
            return "processStatusPage"; // Thymeleaf template for error message
        }

        // Fetch all possible grades for the filter dropdown
        List<Eggradm1> allGrades = eggradm1Repository.findByRowstateGreaterThan((short) 0);
        model.addAttribute("allGrades", allGrades);

        // Fetch the actual student grades for the report
        List<StudentGradeReportDTO> studentGrades = egcrstt1Repository.findStudentGradesForReport(termCourseId, examTypeId);

        // Apply grade-based filtering if selectedGrades are provided
        if (selectedGrades != null && selectedGrades.length > 0) {
            List<String> gradeFilterList = java.util.Arrays.asList(selectedGrades);
            studentGrades = studentGrades.stream()
                .filter(sg -> gradeFilterList.contains(sg.getObtainedGrade().trim()))
                .collect(Collectors.toList());
            model.addAttribute("selectedGrades", selectedGrades); // Keep selected grades for pre-selection in JSP
        }

        if ("True".equalsIgnoreCase(generateExcelSheet)) {
            return generateGradeListExcel(studentGrades, response);
        } else {
            model.addAttribute("studentGrades", studentGrades);
            return "gradeListReport"; // Thymeleaf template for the grade list
        }
    }

    /**
     * Generates an Excel sheet for the Grade List Report.
     */
    private String generateGradeListExcel(List<StudentGradeReportDTO> data, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=grade_list_" + System.currentTimeMillis() + ".xlsx";
        response.setHeader(headerKey, headerValue);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Grade List");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Sr No");
        headerRow.createCell(1).setCellValue("Student Id");
        headerRow.createCell(2).setCellValue("Student Name");
        headerRow.createCell(3).setCellValue("Grade");

        int rowNum = 1;

        for (StudentGradeReportDTO studentGrade : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(rowNum - 1); // Sr No
            row.createCell(1).setCellValue(studentGrade.getStudentInstituteId());
            row.createCell(2).setCellValue(studentGrade.getStudentName());
            row.createCell(3).setCellValue(studentGrade.getObtainedGrade());
        }

        workbook.write(response.getOutputStream());
        workbook.close();

        return null; // No view name needed as content is written directly to response
    }
}