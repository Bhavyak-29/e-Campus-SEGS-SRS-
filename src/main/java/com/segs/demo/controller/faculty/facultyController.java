package com.segs.demo.controller.faculty;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.segs.demo.model.AcademicYear;
import com.segs.demo.model.Address;
import com.segs.demo.model.Batch;
import com.segs.demo.model.Course;
import com.segs.demo.model.Egcrstt1;
import com.segs.demo.model.Eggradm1;
import com.segs.demo.model.Grade;
import com.segs.demo.model.Program;
import com.segs.demo.model.Student;
import com.segs.demo.model.StudentGradeDTO;
import com.segs.demo.model.StudentProfile;
import com.segs.demo.model.StudentRegistrations;
import com.segs.demo.model.StudentSemesterResult;
import com.segs.demo.model.Term;
import com.segs.demo.repository.AddressRepository;
import com.segs.demo.repository.BatchRepository;
import com.segs.demo.repository.Egcrstt1Repository;
import com.segs.demo.repository.Eggradm1Repository;
import com.segs.demo.repository.ProgramRepository;
import com.segs.demo.repository.StudentProfileRepository;
import com.segs.demo.repository.StudentRegistrationCourseRepository;
import com.segs.demo.repository.StudentRegistrationsRepository;
import com.segs.demo.repository.StudentRepository;
import com.segs.demo.repository.StudentSemesterResultRepository;
import com.segs.demo.service.GradeService;
import com.segs.demo.service.facultyService;

import jakarta.servlet.http.HttpSession;

@Controller
public class facultyController {

    @Autowired
    private facultyService facultyService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private AddressRepository addressRepository;

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
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private Egcrstt1Repository Egcrstt1Repository;

    @Autowired
    private Eggradm1Repository Eggradm1Repository;


    @RequestMapping("/directGradeEntry")
    public String directGradeEntry(ModelMap model) {
        List<Term> terms = facultyService.getAllTerms();
        List<Egcrstt1> examTypes = facultyService.getAllExamTypes();
        List<AcademicYear> academicYears = facultyService.getAllAcademicYears();
        List<Course> courses = facultyService.getAllCourses();

        model.addAttribute("terms", terms);
        model.addAttribute("examTypes", examTypes);
        model.addAttribute("academicYears", academicYears);
        model.addAttribute("courses", courses);

        return "directGradeEntry";
    }

    @GetMapping("/directGradeEntry/updatedGrades")
public String showUpdatedGrades(@RequestParam(required = false) List<String> selectedGrades,
                                ModelMap model,
                                HttpSession session) {
    Long trmid = (Long) session.getAttribute("TRMID");
    Long crsid = (Long) session.getAttribute("CRSID");
    Long examTypeId = (Long) session.getAttribute("examTypeId");
    if (crsid == null || trmid == null || examTypeId == null) {
        model.addAttribute("error", "Session expired or missing data. Please reselect options.");
        return "redirect:/directGradeEntry";
    }
    // Load all distinct grades for the filter dropdown
    List<Grade> allGrades = gradeService.getAllGrades();
    List<Grade> distinctGrades = allGrades.stream()
            .collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Grade::getGradeValue))),
                    ArrayList::new));

    model.addAttribute("grades", distinctGrades);

    if (crsid != null && trmid != null && examTypeId != null) {
        List<StudentGradeDTO> updatedGrades = gradeService.getUpdatedStudentGrades(crsid, trmid, examTypeId, selectedGrades);
        model.addAttribute("updatedGrades", updatedGrades);
        model.addAttribute("selectedGrades", selectedGrades);
    } else {
        model.addAttribute("updatedGrades", new ArrayList<StudentGradeDTO>());
    }

    model.addAttribute("CRSID", crsid);
    model.addAttribute("examTypeId", examTypeId);

    return "updatedGradesView";
}

    
    @GetMapping("/directGradeEntry/gradeLeftFrame")
    public String showGradeLeftFrame(HttpSession session, ModelMap model) {
        Long crsid = (Long) session.getAttribute("CRSID");
        Long trmid = (Long) session.getAttribute("TRMID");
        Long examTypeId = (Long) session.getAttribute("examTypeId");

        if (crsid == null || trmid == null || examTypeId == null) {
            model.addAttribute("error", "Session expired or missing data. Please reselect options.");
            return "redirect:/directGradeEntry";
        }

        String termName = gradeService.getTermName(trmid);
        String courseName = gradeService.getCourseName(crsid);

        model.addAttribute("termName", termName);
        model.addAttribute("courseName", courseName);
        return "gradeLeftFrame"; // make sure this matches your Thymeleaf template
    }


    @PostMapping("/directGradeEntry/gradeLeftFrame")
    public String showLeftFrame(
            @RequestParam("AYRID") Long ayrid,
            @RequestParam("TRMID") Long trmid,
            @RequestParam("CRSID") Long crsid,
            @RequestParam("examTypeId") Long examTypeId,
            HttpSession session,
            ModelMap model
    ) {
        session.setAttribute("AYRID", ayrid);
        session.setAttribute("TRMID", trmid);
        session.setAttribute("CRSID", crsid);
        session.setAttribute("examTypeId", examTypeId);
        String termName = gradeService.getTermName(trmid);
        String courseName = gradeService.getCourseName(crsid);

        model.addAttribute("termName", termName);
        model.addAttribute("courseName", courseName);
        return "gradeLeftFrame";
    }
    @GetMapping("/directGradeEntry/grades/chart-view")
    public String showChartView(HttpSession session, ModelMap model) {
        Long crsid = (Long) session.getAttribute("CRSID");
        Long trmid = (Long) session.getAttribute("TRMID");
        Long examTypeId = (Long) session.getAttribute("examTypeId");

        if (crsid == null || trmid == null || examTypeId == null) {
            model.addAttribute("error", "Session expired or missing data. Please reselect options.");
            return "redirect:/directGradeEntry";
        }

        String termName = gradeService.getTermName(trmid);
        String courseName = gradeService.getCourseName(crsid);
        model.addAttribute("termName", termName);
        model.addAttribute("courseName", courseName);

        return "grade_chart"; 
    }
     @GetMapping("/directGradeEntry/grades/chart-view2")
    public String showChartView2(HttpSession session, ModelMap model) {
        Long crsid = (Long) session.getAttribute("CRSID");
        Long trmid = (Long) session.getAttribute("TRMID");
        Long examTypeId = (Long) session.getAttribute("examTypeId");

        if (crsid == null || trmid == null || examTypeId == null) {
            model.addAttribute("error", "Session expired or missing data. Please reselect options.");
            return "redirect:/directGradeEntry";
        }

        String termName = gradeService.getTermName(trmid);
        String courseName = gradeService.getCourseName(crsid);
        model.addAttribute("termName", termName);
        model.addAttribute("courseName", courseName);

        return "grade_chart2"; 
    }
    @GetMapping("/directGradeEntry/grades/chart-view3")
    public String showChartView3(HttpSession session, ModelMap model) {
        Long crsid = (Long) session.getAttribute("CRSID");
        Long trmid = (Long) session.getAttribute("TRMID");
        Long examTypeId = (Long) session.getAttribute("examTypeId");

        if (crsid == null || trmid == null || examTypeId == null) {
            model.addAttribute("error", "Session expired or missing data. Please reselect options.");
            return "redirect:/directGradeEntry";
        }

        String termName = gradeService.getTermName(trmid);
        String courseName = gradeService.getCourseName(crsid);
        model.addAttribute("termName", termName);
        model.addAttribute("courseName", courseName);

        return "grade_chart3"; 
    }

    @RequestMapping("/directGradeEntry/gradeOptions")
    public String showGradeOptions(@RequestParam(required = false) List<String> selectedGrades,
            ModelMap model,
            HttpSession session) {
                Long trmid = (Long) session.getAttribute("TRMID"); 
                Long crsid = (Long) session.getAttribute("CRSID"); 
                Long examTypeId = (Long) session.getAttribute("examTypeId"); 
                if (crsid == null || trmid == null || examTypeId == null) {
                    model.addAttribute("error", "Session expired or missing data. Please reselect options.");
                    return "redirect:/directGradeEntry";
                }
                List<Grade> allGrades = gradeService.getAllGrades();
                List<Grade> distinctGrades = allGrades.stream()
                        .collect(Collectors.collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Grade::getGradeValue))),
                                ArrayList::new));

                model.addAttribute("grades", distinctGrades);

                if (crsid != null && trmid != null && examTypeId != null) {
                    List<StudentGradeDTO> studentGrades = gradeService.getStudentGrades(crsid, trmid, examTypeId, selectedGrades);
                    model.addAttribute("studentGrades", studentGrades);
                    model.addAttribute("selectedGrades", selectedGrades);
                } else {
                    model.addAttribute("studentGrades", new ArrayList<StudentGradeDTO>());
                }
                model.addAttribute("CRSID", crsid); 
                model.addAttribute("examTypeId", examTypeId); 

                return "gradeOptions";
    }


    //StudentInfo -> StudentWise
    @GetMapping("/students/search")
    public String searchStudents(
            @RequestParam(required = false) String fname,
            @RequestParam(required = false) String lname,
            @RequestParam(required = false) String instId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            ModelMap model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Student> studentsPage;

        if (instId != null) {
            studentsPage = studentRepository.findByStdinstidAndStdrowstateGreaterThan(instId, 0, pageable);
        } else if (fname != null && lname != null) {
            studentsPage = studentRepository.findByStdfirstnameContainingIgnoreCaseAndStdlastnameContainingIgnoreCaseAndStdrowstateGreaterThan(fname, lname, 0, pageable);
        } else if (fname != null) {
            studentsPage = studentRepository.findByStdfirstnameContainingIgnoreCaseAndStdrowstateGreaterThan(fname, 0, pageable);
        } else if (lname != null) {
            studentsPage = studentRepository.findByStdlastnameContainingIgnoreCaseAndStdrowstateGreaterThan(lname, 0, pageable);
        } else {
            studentsPage = Page.empty(pageable);
        }

        model.addAttribute("studentsPage", studentsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentsPage.getTotalPages());
        model.addAttribute("totalItems", studentsPage.getTotalElements());

        return "student_search";
    }

    @GetMapping("/students/{id}")
    public String showStudentInfo(
            @PathVariable String id,
            @RequestParam(value = "from", required = false) String from,
            Model model) {
    
        List<Student> students = studentRepository.findStudentByInstIdWithLatestRegistration(id);
        if (students.isEmpty()) return "error/404";
        Student student = students.get(0);
    
        StudentProfile profile = studentProfileRepository.findBystdid(student.getStdid());
    
        List<Long> addressIds = new ArrayList<>();
        if (student.getPrmtAdrId() != null) addressIds.add(student.getPrmtAdrId());
        if (student.getCurrAdrId() != null) addressIds.add(student.getCurrAdrId());
    
        List<Address> addresses = addressRepository.findAddressesByIdsWithCustomOrder(
                addressIds, student.getCurrAdrId(), student.getPrmtAdrId());
    
        Address currAddr = addresses.stream()
                .filter(a -> a.getAdrid().equals(student.getCurrAdrId()))
                .findFirst().orElse(null);
    
        Address permAddr = addresses.stream()
                .filter(a -> a.getAdrid().equals(student.getPrmtAdrId()))
                .findFirst().orElse(null);
    
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
    
        // === Egcrstt1 Result Grouping ===
        List<Egcrstt1> resultRecords = Egcrstt1Repository.findAllById_StudId(student.getStdid());
    
        Map<Long, List<Egcrstt1>> resultsGrouped = resultRecords.stream()
                .filter(r -> !"D".equalsIgnoreCase(r.getRowStatus()))
                .collect(Collectors.groupingBy(r -> r.getId().getTcrid(), LinkedHashMap::new, Collectors.toList()));
    
        // === Grade + Exam Title Table Data ===
        Map<Long, List<Object[]>> gradeExamMap = new LinkedHashMap<>();
        for (Long tcrid : resultsGrouped.keySet()) {
            List<Object[]> gradeAndTitles = Egcrstt1Repository.findGradeAndExamTitle(student.getStdid(), tcrid);
            gradeExamMap.put(tcrid, gradeAndTitles);
        }
    
        // === Grade Info Map (ID to Grade Master) ===
        Map<Integer, Eggradm1> gradeMap = Eggradm1Repository.findAll().stream()
                .filter(g -> g.getGrad_id() != null)
                .collect(Collectors.toMap(g -> g.getGrad_id().intValue(), g -> g));
    
        // === Model Attributes ===
        model.addAttribute("student", student);
        model.addAttribute("profile", profile);
        model.addAttribute("permAddress", permAddr);
        model.addAttribute("currAddress", currAddr);
        model.addAttribute("registrations", registrations);
        model.addAttribute("regCourses", regCourses);
        model.addAttribute("regResults", regResults);
        model.addAttribute("fromPage", from);
    
        model.addAttribute("egcrResults", resultsGrouped);          // grouped raw Egcrstt1 rows
        model.addAttribute("gradeMap", gradeMap);                   // grade ID → object
        model.addAttribute("gradeExamMap", gradeExamMap);           // tcrid → list of [grad_lt, examtype_title]
    
        return "student_details";
    }
    

    // StudentInfo → Batchwise
    @GetMapping("/students/batch")
    public String showProgramAndBatchSelection(@RequestParam(value = "programId", required = false) Long programId,
                                            Model model) {
        List<Program> programs = programRepository.findAll();
        programs.sort(Comparator.comparing(Program::getPrgname));
        model.addAttribute("programs", programs);

        if (programId != null) {
            Program program = programRepository.findById(programId).orElse(null);
            if (program != null) {
                List<Batch> batches = batchRepository.findByProgram(program);
                batches.sort(Comparator.comparing(Batch::getBchname).reversed());
                model.addAttribute("selectedProgram", program);
                model.addAttribute("batches", batches);
            }
        }

        return "batchwise_search";
    }
    @GetMapping("/students/batch/list")
    public String viewStudents(@RequestParam("programId") Long programId,
                            @RequestParam("batchId") Long batchId,
                            Model model) {
        Program program = programRepository.findById(programId).orElse(null);
        Batch batch = batchRepository.findById(batchId).orElse(null);
        if (program == null || batch == null) return "redirect:/students/batch";


        List<Student> students = studentRepository.findByBatch(batch);
        students.sort(Comparator.comparing(Student::getStdinstid));
        model.addAttribute("program", program);
        model.addAttribute("batch", batch);
        model.addAttribute("students", students);
        return "batchwise_details";
    }    
}
