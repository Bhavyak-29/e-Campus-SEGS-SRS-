package com.ec2.main.controller.faculty;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ec2.main.model.CourseDTO;
import com.ec2.main.model.ExamType;
import com.ec2.main.model.Terms;
import com.ec2.main.service.AcademicService;

import jakarta.servlet.http.HttpSession;



@RestController
@RequestMapping("/api")
public class GradeEntryApiController {

    @Autowired
    private AcademicService academicService;

    @GetMapping("/terms")
    public List<Terms> getTermsByAcademicYear(@RequestParam Long AYRID) {
        return academicService.getTermsByAcademicYear(AYRID);
    }

    @GetMapping("/courses")
    public List<CourseDTO> getCoursesByTerm(
            @RequestParam Long TRMID,
            HttpSession session) {
                
        Long userId = (Long) session.getAttribute("userid"); // get userid from session
    if (userId == 99001L) {
        return academicService.getCoursesByTerm(TRMID);
    }
        if (userId == null) {
            throw new RuntimeException("User ID not found in session.");
        }

        return academicService.getCoursesByTermAndFaculty(TRMID, userId);
    }

    @GetMapping("/exam-types")
    public List<ExamType> getExamTypes(@RequestParam Long CRSID, @RequestParam Long TRMID) {
        return academicService.getExamTypesByCourseAndTerm(CRSID, TRMID);
    }
    
}
