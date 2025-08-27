package com.segs.demo.controller.faculty;

import java.io.IOException; // Assuming AcademicYear, Term, Course, TermCourse, ExamType are here
import java.math.BigDecimal; // Assuming new repositories are in this package
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import com.segs.demo.model.AcademicYear;
import com.segs.demo.model.Batch;
import com.segs.demo.model.DropdownItem;
import com.segs.demo.model.Egcrstt1;
import com.segs.demo.model.Eggradm1;
import com.segs.demo.model.ExamType;
import com.segs.demo.model.Grade;
import com.segs.demo.model.Program;
import com.segs.demo.model.Semester;
import com.segs.demo.model.Student;
import com.segs.demo.model.StudentGradeDTO;
import com.segs.demo.model.StudentGradeReportDTO;
import com.segs.demo.model.StudentRegistrations;
import com.segs.demo.model.StudentSemesterResult;
import com.segs.demo.model.Term;
import com.segs.demo.model.TermCourse;
import com.segs.demo.repository.AcademicYearRepository;
import com.segs.demo.repository.BatchRepository;
import com.segs.demo.repository.Egcrstt1Repository;
import com.segs.demo.repository.Eggradm1Repository;
import com.segs.demo.repository.ExamTypeRepository;
import com.segs.demo.repository.ProgramRepository;
import com.segs.demo.repository.SemesterRepository;
import com.segs.demo.repository.StudentRegistrationCourseRepository;
import com.segs.demo.repository.StudentRegistrationsRepository;
import com.segs.demo.repository.StudentRepository;
import com.segs.demo.repository.StudentSemesterResultRepository;
import com.segs.demo.repository.TermCourseRepository;
import com.segs.demo.repository.TermRepository;
import com.segs.demo.service.GradeService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;



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


    @Autowired
    private GradeService gradeService;

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

        List<AcademicYear> academicYears = academicYearRepository.findByRowStateGreaterThanOrderByField1Asc(0);
        List<DropdownItem> academicYearDropdown = academicYears.stream()
            .map(ay -> new DropdownItem(String.valueOf(ay.getId()), ay.getName()))
            .collect(Collectors.toList());

        model.addAttribute("academicYears", academicYearDropdown);
        model.addAttribute("actionText", actionText);
        model.addAttribute("targetUrl", targetUrl);
        model.addAttribute("baseApiUrl", "/results/coursewise/exam-specific/api");

        return "courseExamSelector";
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

    /*
     * Coursewise -> Updated Grade Functionality
     */
    @GetMapping("/coursewise/updated-grade/selector")
    public String showUpdatedGradeSelector(Model model, HttpSession session,
                                        @RequestParam(name = "actiontext", required = false) String actionText,
                                        @RequestParam(name = "target", required = false, defaultValue = "/results/coursewise/updated-grade/list") String targetUrl,
                                        @RequestParam(name = "fromWhichSystem", required = false) String fromWhichSystem) {

        List<AcademicYear> academicYears = academicYearRepository.findByRowStateGreaterThanOrderByField1Asc(0);
        List<DropdownItem> academicYearDropdown = academicYears.stream()
                .map(ay -> new DropdownItem(String.valueOf(ay.getId()), ay.getName()))
                .collect(Collectors.toList());

        model.addAttribute("academicYears", academicYearDropdown);
        model.addAttribute("actionText", actionText);
        model.addAttribute("targetUrl", targetUrl);
        model.addAttribute("baseApiUrl", "/results/coursewise/updated-grade/api");

        return "courseExamSelector";
    }


    /**
     * AJAX endpoint to get terms based on selected academic year.
     * This method is general and does not specifically filter for 'updated' grades at the term level.
     */
    @GetMapping("/coursewise/updated-grade/api/terms")
    @ResponseBody
    public List<DropdownItem> getTermsForUpdatedGradesByAcademicYear(@RequestParam Long academicYearId) {
        // This remains general, fetching all active terms for the academic year
        List<Term> terms = termRepository.findByAcademicYear_IdAndRowStateGreaterThanOrderByField1Asc(academicYearId, (int) 0);
        return terms.stream()
                .map(term -> new DropdownItem(String.valueOf(term.getId()), term.getName()))
                .collect(Collectors.toList());
    }

    /**
     * AJAX endpoint to get courses based on selected term that have updated grades.
     * Calls the `GradeService` method, which in turn uses `TermCourseRepository.findUpdatedTermCoursesByTermId`.
     */
    @GetMapping("/coursewise/updated-grade/api/courses")
    @ResponseBody
    public List<DropdownItem> getUpdatedCoursesByTerm(@RequestParam Long termId) {
        return gradeService.getUpdatedTermCoursesByTermId(termId);
    }

    /**
     * AJAX endpoint to get exam types based on selected course (TermCourseId) that have updated grades.
     * Calls the `GradeService` method, which in turn uses `Egcrstt1Repository.findExamTypesWithUpdatedGradesByTermCourseId`.
     */
    @GetMapping("/coursewise/updated-grade/api/examtypes")
    @ResponseBody
    public List<DropdownItem> getExamTypesForUpdatedGradesByCourse(@RequestParam Long termCourseId) {
        return gradeService.getExamTypesWithUpdatedGradesByTermCourseId(termCourseId);
    }

    /**
     * Displays the updated grade list report based on selected Academic Year, Term, Course, and Exam Type.
     * Parameters are expected to come directly from the form submission of 'courseExamSelector.html'.
     */
    @GetMapping("/coursewise/updated-grade/list")
    public String showUpdatedGradeListReport(Model model, HttpSession session, HttpServletResponse response,
                                             @RequestParam(name = "academicYearId") Long academicYearId, // Required parameter from form
                                             @RequestParam(name = "termId") Long termId, // Required parameter from form
                                             @RequestParam(name = "termCourseId") Long termCourseId, // Required parameter from form
                                             @RequestParam(name = "examTypeId") Long examTypeId, // Required parameter from form
                                             @RequestParam(required = false) List<String> selectedGrades,
                                             @RequestParam(name = "generateExcelSheet", required = false) String generateExcelSheet,
                                             RedirectAttributes redirectAttributes) throws IOException {

        // Basic validation (Spring will already handle missing required params by throwing an exception,
        // but this adds a clean redirect with a message if somehow they are null or if a direct URL access bypasses form validation)

        if (academicYearId == null || termId == null || termCourseId == null || examTypeId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select all required fields (Academic Year, Term Name, Course Name, Exam Type).");
            return "redirect:/results/coursewise/updated-grade/selector";
        }

        // Check if updated grades exist for the selected criteria
        boolean isGradeUpdated = egcrstt1Repository.existsById_TcridAndId_ExamtypeIdAndUpdatedByIsNotNullAndUpdatedDateIsNotNull(termCourseId, examTypeId);

        if (!isGradeUpdated) {
            model.addAttribute("errorMessage", "No updated grades found for the selected course and exam type.");
            return "processStatusPage"; // A generic page to show status messages
        }

        // Fetch all possible grades for the filter dropdown on the report page
        List<Grade> allGrades = gradeService.getAllGrades();
        model.addAttribute("allGrades", allGrades);

        //crsid, trmid, examTypeId, selectedGrades
        // Call the GradeService method to fetch the actual updated student grades for the report
        TermCourse tc = termCourseRepository.findById(termCourseId)
        .orElseThrow(() -> new EntityNotFoundException("TermCourse not found"));    
        List<StudentGradeDTO> studentGrades = gradeService.getUpdatedStudentGrades(tc.getCourse().getId(),termId,examTypeId, selectedGrades);

        model.addAttribute("studentGrades", studentGrades);
        model.addAttribute("selectedGrades", selectedGrades); // To retain selections in the filter dropdown on the report page

        // Add display names for the report header/title
        AcademicYear selectedAy = academicYearRepository.findById(academicYearId).orElse(null);
        Term selectedTerm = termRepository.findById(termId).orElse(null);
        TermCourse selectedTc = termCourseRepository.findById(termCourseId).orElse(null);
        ExamType selectedEt = examTypeRepository.findExamTypeById(examTypeId).orElse(null); // Assuming this method exists in Egcrstt1Repository or a dedicated ExamTypeRepository

        model.addAttribute("selectedAcademicYearName", selectedAy != null ? selectedAy.getName() : "N/A");
        model.addAttribute("selectedTermName", selectedTerm != null ? selectedTerm.getName() : "N/A");
        // Ensure getCourse() and getName() exist on TermCourse and Course entities respectively
        model.addAttribute("selectedCourseName", selectedTc != null && selectedTc.getCourse() != null ? selectedTc.getCourse().getName() : "N/A");
        // Ensure getExamtypeName() exists on your ExamType entity
        model.addAttribute("selectedExamTypeName", selectedEt != null ? selectedEt.getTitle() : "N/A");
        model.addAttribute("AcademicYearId", academicYearId);
        model.addAttribute("TermId", termId);
        model.addAttribute("termCourseId", termCourseId);
        model.addAttribute("examTypeId", examTypeId);

        // Handle Excel generation if requested
        if ("True".equalsIgnoreCase(generateExcelSheet)) {
            return generateUpdatedGradeListExcel(studentGrades, response);
        } else {
            return "updatedGradeListReport"; // Display the HTML report
        }
    }

    /**
     * Helper method to generate an Excel sheet from the list of StudentGradeDTOs.
     * This method remains largely the same as it consumes the DTO.
     */
    private String generateUpdatedGradeListExcel(List<StudentGradeDTO> data, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=updated_grade_list_" + System.currentTimeMillis() + ".xlsx";
        response.setHeader(headerKey, headerValue);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Updated Grade List");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Sr No");
        headerRow.createCell(1).setCellValue("Student ID");
        headerRow.createCell(2).setCellValue("Student Name");
        headerRow.createCell(3).setCellValue("Grade");

        int rowNum = 1;

        for (StudentGradeDTO studentGrade : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(rowNum - 1); // Serial number
            row.createCell(1).setCellValue(studentGrade.getStudentId());
            row.createCell(2).setCellValue(studentGrade.getStudentName());
            row.createCell(3).setCellValue(studentGrade.getGrade());
        }

        workbook.write(response.getOutputStream());
        workbook.close();

        return null; // Spring handles the response directly, no view name needed
    }
}