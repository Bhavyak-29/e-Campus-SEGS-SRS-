package com.ec2.main.controller.srs;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ec2.main.RegistrationConstants;
import com.ec2.main.model.Courses;
import com.ec2.main.model.StudentRegistrationCourses;
import com.ec2.main.model.StudentRegistrations;
import com.ec2.main.model.TermCourses;
import com.ec2.main.service.CourseAddDropSaveService;
import com.ec2.main.service.StudentRegistrationEditService;

@Controller
public class CourseAddDropSaveController {

    @Autowired
    private StudentRegistrationEditService editService;

    @Autowired
    private CourseAddDropSaveService saveCADService;

    @PostMapping("/srs/CourseAddDropSave")
    public String submitRegistration(
            @RequestParam Long studentId,
            @RequestParam Long semesterId,
            @RequestParam Long termId,
            @RequestParam String semesterName,
            @RequestParam List<Long> courseIds,
            @RequestParam(required = false) String remarks,
            @RequestParam(required = false) String authority,
            @RequestParam MultiValueMap<String, String> allParams,
            Model model) {

        StudentRegistrations srg = editService.getRegistrationsByStudentIdandSemesterId(studentId, semesterId);

        Long srgid=srg.getSrgid();

        List<StudentRegistrationCourses> strgcrs = new ArrayList<>();
        strgcrs = editService.getStudentRegistrationCourses(srgid);

        Set<String> regular = new HashSet<>();
        Set<String> backlog = new HashSet<>();
        Set<String> gradeImprovement = new HashSet<>();
        Set<String> audit = new HashSet<>();

        for (StudentRegistrationCourses src : strgcrs) {
            Long termcourseId = src.getSrctcrid();
            
            TermCourses tc = editService.getTermCourse(termcourseId);
            Courses course = editService.getCourseById(tc.getTcrcrsid());
            System.out.println("crsname: " + course.getCrsname());
            System.out.println("type: " + src.getSrctype());
            if (course != null && "REGULAR".equals(src.getSrctype())) {
                regular.add("" + course.getCrsid());
            }
            else if(course != null && "BACKLOG".equals(src.getSrctype())) {
                backlog.add("" + course.getCrsid());
            }
            else if(course != null && "GRADE IMPROVEMENT".equals(src.getSrctype())){
                gradeImprovement.add("" + course.getCrsid());
            }
            else if(course != null && "AUDIT".equals(src.getSrctype())){
                audit.add("" + course.getCrsid());
            }
        }

        Set<String> selectedregularIds = new HashSet<>();
        selectedregularIds.addAll(allParams.getOrDefault("selectedCourseIds", Collections.emptyList()));
        selectedregularIds.addAll(allParams.getOrDefault("selectedOtherCourses", Collections.emptyList()));

        Set<String> selectedbacklogIds = new HashSet<>();
        selectedbacklogIds.addAll(allParams.getOrDefault("selectedBacklogCourses", Collections.emptyList()));

        Set<String> selectedgradeImprovementIds = new HashSet<>();
        selectedgradeImprovementIds.addAll(allParams.getOrDefault("selectedGradeImprovementCourses", Collections.emptyList()));

        Set<String> selectedauditIds = new HashSet<>();
        selectedauditIds.addAll(allParams.getOrDefault("selectedAuditCourses", Collections.emptyList()));



        Set<String> deselectedregularCourses = new HashSet<>(regular);
        deselectedregularCourses.removeAll(selectedregularIds);

        Set<String> newlySelectedregularCourses = new HashSet<>(selectedregularIds);
        newlySelectedregularCourses.removeAll(regular);

        Set<String> deselectedbacklogCourses = new HashSet<>(backlog);
        deselectedbacklogCourses.removeAll(selectedbacklogIds);

        Set<String> newlySelectedbacklogCourses = new HashSet<>(selectedbacklogIds);
        newlySelectedbacklogCourses.removeAll(backlog);

        Set<String> deselectedgradeImprovementCourses = new HashSet<>(gradeImprovement);
        deselectedgradeImprovementCourses.removeAll(selectedgradeImprovementIds);

        Set<String> newlySelectedgradeImprovementCourses = new HashSet<>(selectedgradeImprovementIds);
        newlySelectedgradeImprovementCourses.removeAll(gradeImprovement);

        Set<String> deselectedauditCourses = new HashSet<>(audit);
        deselectedauditCourses.removeAll(selectedauditIds);

        Set<String> newlySelectedauditCourses = new HashSet<>(selectedauditIds);
        newlySelectedauditCourses.removeAll(audit);

        saveCADService.removeRegularCourseIds(deselectedregularCourses,srgid,termId,semesterId,studentId);
        saveCADService.removeBacklogCourseIds(deselectedbacklogCourses,srgid,termId,semesterId,studentId);
        saveCADService.removeGradeImprovementCourseIds(deselectedgradeImprovementCourses,srgid,termId,semesterId,studentId);
        saveCADService.removeAuditCourseIds(deselectedauditCourses,srgid,termId,semesterId,studentId);

        saveCADService.addNewRegularCourseIds(newlySelectedregularCourses,srgid,termId,semesterId,studentId);
        saveCADService.addNewBacklogCourseIds(newlySelectedbacklogCourses,srgid,termId,semesterId,studentId);
        saveCADService.addNewGradeImprovementCourseIds(newlySelectedgradeImprovementCourses,srgid,termId,semesterId,studentId);
        saveCADService.addNewAuditCourseIds(newlySelectedauditCourses,srgid,termId,semesterId,studentId);


        // Add attributes for confirmation page
        model.addAttribute("semesterName", semesterName); // or fetch from DB
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("studentId", studentId);
        
        return RegistrationConstants.JSPCOURSEADDDROPSAVE;
    }
}
