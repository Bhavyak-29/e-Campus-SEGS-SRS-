package com.ec2.main.controller.srs;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ec2.main.RegistrationConstants;
import com.ec2.main.config.RegistrationDeadlineConfig;
import com.ec2.main.model.Semesters;
import com.ec2.main.model.StudentRegistrations;
import com.ec2.main.model.Students;
import com.ec2.main.service.StudentRegistrationService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CourseDropController {
    @Autowired
    private StudentRegistrationService registrationService;

    @Autowired
    private RegistrationDeadlineConfig deadlineConfig;

    @GetMapping("/srs/CourseDropList")
    public String listStudentRegistrations(
            // @RequestParam(name = RegistrationConstants.PARAM_STUDENT_ID, required = true) Long studentId,
            HttpSession session,
            Model model) {

        Long studentId = (Long) session.getAttribute("studentId");

        Students st = registrationService.getStudentById(studentId);

        List<Semesters> smt = registrationService.getSemesterById(st.getStdbchid());

        List<StudentRegistrations> regs = registrationService.getRegistrationsByStudentId(studentId);

        Map<Long, StudentRegistrations> registrationsMap = regs.stream()
                .collect(Collectors.toMap(StudentRegistrations::getSrgstrid, Function.identity()));
        
        boolean addDropisWithinDeadline = deadlineConfig.dropisWithinDeadline();

        Long maxStrid = registrationService.getMaxSemesterId(st.getStdbchid());

        model.addAttribute("isWithinDeadline", addDropisWithinDeadline);
        model.addAttribute("maxStrid", maxStrid);
        model.addAttribute("studentbean", st);
        model.addAttribute("semestersbean", smt);
        model.addAttribute(RegistrationConstants.ATTRIBUTESTUDENTREGISTRATIONS,registrationsMap);
        return RegistrationConstants.JSPCOURSEDROPLIST;
    }   
}
