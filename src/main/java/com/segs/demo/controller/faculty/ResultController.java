package com.segs.demo.controller.faculty;

import com.segs.demo.model.Egcrstt1;
import com.segs.demo.model.Eggradm1;
import com.segs.demo.model.Student;
import com.segs.demo.model.StudentRegistrations;
import com.segs.demo.model.StudentSemesterResult;
import com.segs.demo.model.Program;
import com.segs.demo.model.Batch;
import com.segs.demo.model.Semester;

import com.segs.demo.repository.Egcrstt1Repository;
import com.segs.demo.repository.Eggradm1Repository;
import com.segs.demo.repository.StudentRegistrationCourseRepository;
import com.segs.demo.repository.StudentRegistrationsRepository;
import com.segs.demo.repository.StudentRepository;
import com.segs.demo.repository.StudentSemesterResultRepository;
import com.segs.demo.repository.ProgramRepository;
import com.segs.demo.repository.BatchRepository;
import com.segs.demo.repository.SemesterRepository;

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

// Apache POI for Excel export
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


@Controller
@RequestMapping("/results") // Base path for all result-related functionalities
public class ResultController {

    // Repositories
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
    private StudentSemesterResultRepository studentSemesterResultRepository; // Note: This repository now has a custom implementation for SPI/CPI list
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private BatchRepository batchRepository;
    @Autowired
    private SemesterRepository semesterRepository;


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


    // --- SPI-CPI List Functionality ---

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
                            
        if (semesterId == null) { // This check is redundant if previous blocks handle null/empty, but good for safety
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
}