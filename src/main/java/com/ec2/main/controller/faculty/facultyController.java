package com.ec2.main.controller.faculty;

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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.ec2.main.model.AcademicYears;
import com.ec2.main.model.Addresses;
import com.ec2.main.model.Batches;
import com.ec2.main.model.Courses;
import com.ec2.main.model.Egcrstt1;
import com.ec2.main.model.Eggradm1;
import com.ec2.main.model.Grade;
import com.ec2.main.model.Programs;
import com.ec2.main.model.StudentGradeDTO;
import com.ec2.main.model.StudentProfile;
import com.ec2.main.model.StudentRegistration;
import com.ec2.main.model.StudentSemesterResult;
import com.ec2.main.model.Students;
import com.ec2.main.model.Terms;
import com.ec2.main.repository.AddressesRepository;
import com.ec2.main.repository.BatchesRepository;
import com.ec2.main.repository.Egcrstt1Repository;
import com.ec2.main.repository.Eggradm1Repository;
import com.ec2.main.repository.ProgramsRepository;
import com.ec2.main.repository.StudentProfileRepository;
import com.ec2.main.repository.StudentRegistrationCoursesRepository;
import com.ec2.main.repository.StudentRegistrationRepository;
import com.ec2.main.repository.StudentSemesterResultRepository;
import com.ec2.main.repository.StudentsRepository;
import com.ec2.main.service.GradeService;
import com.ec2.main.service.facultyService;

import jakarta.servlet.http.HttpSession;

@Controller
public class facultyController {

    @Autowired
    private facultyService facultyService;

    @Autowired
    private StudentsRepository studentsRepository;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private AddressesRepository addressesRepository;

    @Autowired
    private StudentRegistrationRepository studentRegistrationRepository;

    @Autowired
    private StudentRegistrationCoursesRepository studentRegistrationCoursesRepository;

    @Autowired
    private StudentSemesterResultRepository studentSemesterResultRepository;

    @Autowired
    private ProgramsRepository programsRepository;

    @Autowired
    private BatchesRepository batchesRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private Egcrstt1Repository Egcrstt1Repository;

    @Autowired
    private Eggradm1Repository Eggradm1Repository;

    @RequestMapping("/directGradeEntry")
    public String directGradeEntry(ModelMap model) {
        List<AcademicYears> academicYears = facultyService.getAllAcademicYears();
        List<Terms> terms = facultyService.getAllTerms();
        List<Courses> courses = facultyService.getAllCourses();
        List<Egcrstt1> examTypes = facultyService.getAllExamTypes();
        AcademicYears latestAY = academicYears.stream()
                .max(Comparator.comparingLong(AcademicYears::getAyrid))
                .orElse(null);
        Terms latestTerm = null;
        if (latestAY != null) {
            latestTerm = terms.stream()
                    .filter(t -> t.getAcademicYear().getAyrid().equals(latestAY.getAyrid()))
                    .max(Comparator.comparingLong(Terms::getTrmid))
                    .orElse(null);
        }

        model.addAttribute("academicYears", academicYears);
        model.addAttribute("terms", terms);
        model.addAttribute("courses", courses);
        model.addAttribute("examTypes", examTypes);

        model.addAttribute("latestAY", latestAY != null ? latestAY.getAyrid() : null);
        model.addAttribute("latestTerm", latestTerm != null ? latestTerm.getTrmid() : null);

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
        return "gradeLeftFrame";
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
                    studentGrades = studentGrades.stream()
                    .filter(dto -> dto.getGrade() != null && !dto.getGrade().equalsIgnoreCase("NULL"))
                    .collect(Collectors.toList());
                    model.addAttribute("studentGrades", studentGrades);
                    model.addAttribute("selectedGrades", selectedGrades);
                } else {
                    model.addAttribute("studentGrades", new ArrayList<StudentGradeDTO>());
                }
                model.addAttribute("CRSID", crsid); 
                model.addAttribute("examTypeId", examTypeId); 

                return "gradeOptions";
    }

    @GetMapping("/students/search")
    public String searchStudents(
            @RequestParam(required = false) String fname,
            @RequestParam(required = false) String lname,
            @RequestParam(required = false) String instId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            ModelMap model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Students> studentsPage;

        if (instId != null) {
            studentsPage = studentsRepository.findByStdinstidAndStdrowstateGreaterThan(instId, 0, pageable);
        } else if (fname != null && lname != null) {
            studentsPage = studentsRepository.findByStdfirstnameContainingIgnoreCaseAndStdlastnameContainingIgnoreCaseAndStdrowstateGreaterThan(fname, lname, 0, pageable);
        } else if (fname != null) {
            studentsPage = studentsRepository.findByStdfirstnameContainingIgnoreCaseAndStdrowstateGreaterThan(fname, 0, pageable);
        } else if (lname != null) {
            studentsPage = studentsRepository.findByStdlastnameContainingIgnoreCaseAndStdrowstateGreaterThan(lname, 0, pageable);
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
    
        List<Students> students = studentsRepository.findStudentByInstIdWithLatestRegistration(id);
        
        if (students.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        Students student = students.get(0);
        StudentProfile profile = studentProfileRepository.findByStdid(student.getStdid());
        List<Long> addressIds = new ArrayList<>();
        if (student.getPrmtAdrId() != null) addressIds.add(student.getPrmtAdrId());
        if (student.getCurrAdrId() != null) addressIds.add(student.getCurrAdrId());
    
        List<Addresses> addresses = addressesRepository.findAddressesByIdsWithCustomOrder(
                addressIds, student.getCurrAdrId(), student.getPrmtAdrId());
    
        Addresses currAddr = addresses.stream()
                .filter(a -> a.getAdrid().equals(student.getCurrAdrId()))
                .findFirst().orElse(null);
    
        Addresses permAddr = addresses.stream()
                .filter(a -> a.getAdrid().equals(student.getPrmtAdrId()))
                .findFirst().orElse(null);
    
        List<StudentRegistration> registrations = studentRegistrationRepository
                .findAllRegistrationsByStudentIdOrderBySemesterSequence(student.getStdid());
        Map<Long, List<Object[]>> regCourses = new LinkedHashMap<>();
        Map<Long, List<StudentSemesterResult>> regResults = new LinkedHashMap<>();
    
        for (StudentRegistration reg : registrations) {
            regCourses.put(reg.getSrgid(), studentRegistrationCoursesRepository
                    .findActiveRegistrationCourseDetails(reg.getSrgid()));
            //System.out.println("Courses for regId " + reg.getSrgid() + ": " + reg);
            regResults.put(reg.getSrgid(), studentSemesterResultRepository
                    .findByStudentRegistration_SrgidAndRowStateGreaterThan(reg.getSrgid(), (short) 0));
        }
    
        List<Egcrstt1> resultRecords = Egcrstt1Repository.findAllById_StudId(student.getStdid());
    
        Map<Long, List<Egcrstt1>> resultsGrouped = resultRecords.stream()
                .filter(r -> !"D".equalsIgnoreCase(r.getRowStatus()))
                .collect(Collectors.groupingBy(r -> r.getId().getTcrid(), LinkedHashMap::new, Collectors.toList()));
    
        Map<Long, List<Object[]>> gradeExamMap = new LinkedHashMap<>();
        for (Long tcrid : resultsGrouped.keySet()) {
            List<Object[]> gradeAndTitles = Egcrstt1Repository.findGradeAndExamTitle(student.getStdid(), tcrid);
            gradeExamMap.put(tcrid, gradeAndTitles);
        }
    
        Map<Integer, Eggradm1> gradeMap = Eggradm1Repository.findAll().stream()
                .filter(g -> g.getGrad_id() != null)
                .collect(Collectors.toMap(g -> g.getGrad_id().intValue(), g -> g));
    
        model.addAttribute("student", student);
        model.addAttribute("profile", profile);
        model.addAttribute("permAddress", permAddr);
        model.addAttribute("currAddress", currAddr);
        model.addAttribute("registrations", registrations);
        model.addAttribute("regCourses", regCourses);
        model.addAttribute("regResults", regResults);
        model.addAttribute("fromPage", from);
    
        model.addAttribute("egcrResults", resultsGrouped);  
        model.addAttribute("gradeMap", gradeMap);  
        model.addAttribute("gradeExamMap", gradeExamMap);

        return "student_details";
    }
    
    @GetMapping("/students/batch")
    public String showProgramAndBatchSelection(@RequestParam(value = "programId", required = false) Long programId,
                                            Model model) {
        List<Programs> programs = programsRepository.findAll();
        programs.sort(Comparator.comparing(Programs::getPrgname));
        model.addAttribute("programs", programs);

        if (programId != null) {
            Programs program = programsRepository.findById(programId).orElse(null);
            if (program != null) {
                List<Batches> batches = batchesRepository.findByPrograms(program);
                batches.sort(Comparator.comparing(Batches::getBchname).reversed());
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
        Programs program = programsRepository.findById(programId).orElse(null);
        Batches batch = batchesRepository.findById(batchId).orElse(null);
        if (program == null || batch == null) return "redirect:/students/batch";


        List<Students> students = studentsRepository.findByBatch(batch);
        students.sort(Comparator.comparing(Students::getStdinstid));
        model.addAttribute("program", program);
        model.addAttribute("batch", batch);
        model.addAttribute("students", students);
        return "batchwise_details";
    }    
}
