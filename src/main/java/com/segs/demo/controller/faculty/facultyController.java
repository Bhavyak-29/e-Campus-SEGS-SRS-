package com.segs.demo.controller.faculty;

import com.segs.demo.model.*;
import com.segs.demo.repository.*;
import com.segs.demo.service.facultyService;
import com.segs.demo.service.GradeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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
    private StudentRegistrationRepository studentRegistrationRepository;

    @Autowired
    private StudentRegistrationsRepository studentRegistrationsRepository;

    @Autowired
    private StudentRegistrationCourseRepository studentRegistrationCourseRepository;

    @Autowired
    private StudentSemesterResultRepository studentSemesterResultRepository;

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

    @RequestMapping("/directGradeEntry/gradeOptions")
    public String showGradeOptions(
            @RequestParam(required = false) Long CRSID,
            @RequestParam(required = false) Long examTypeId,
            @RequestParam(required = false) List<String> selectedGrades,
            ModelMap model) {

        List<Grade> allGrades = gradeService.getAllGrades();
        List<Grade> distinctGrades = allGrades.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Grade::getGradeValue))),
                        ArrayList::new));

        model.addAttribute("grades", distinctGrades);

        if (CRSID != null && examTypeId != null) {
            List<StudentGradeDTO> studentGrades = gradeService.getStudentGrades(CRSID, examTypeId, selectedGrades);
            model.addAttribute("studentGrades", studentGrades);
            model.addAttribute("selectedGrades", selectedGrades);
        }

        model.addAttribute("CRSID", CRSID);
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
    public String showStudentInfo(@PathVariable String id, Model model) {
        List<Student> students = studentRepository.findStudentByInstIdWithLatestRegistration(id);
        if (students.isEmpty()) return "error/404";
        Student student = students.get(0);

        List<Long> addressIds = new ArrayList<>();
        if (student.getPrmtAdrId() != null) addressIds.add(student.getPrmtAdrId());
        if (student.getCurrAdrId() != null) addressIds.add(student.getCurrAdrId());

        List<Address> addresses = addressRepository.findAddressesByIdsWithCustomOrder(
                addressIds,
                student.getCurrAdrId(),
                student.getPrmtAdrId()
        );

        Address currAddr = addresses.stream().filter(a -> a.getAdrid().equals(student.getCurrAdrId())).findFirst().orElse(null);
        Address permAddr = addresses.stream().filter(a -> a.getAdrid().equals(student.getPrmtAdrId())).findFirst().orElse(null);

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

        model.addAttribute("student", student);
        model.addAttribute("permAddress", permAddr);
        model.addAttribute("currAddress", currAddr);
        model.addAttribute("registrations", registrations);
        model.addAttribute("regCourses", regCourses);
        model.addAttribute("regResults", regResults);

        return "student_details";
    }
}
