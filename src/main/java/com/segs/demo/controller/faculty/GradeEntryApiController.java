package com.segs.demo.controller.faculty;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.segs.demo.model.CourseDTO;
import com.segs.demo.model.ExamType;
import com.segs.demo.model.Term;
import com.segs.demo.service.AcademicService;
import com.segs.demo.service.GradeService;

import jakarta.servlet.http.HttpSession;



@RestController
@RequestMapping("/api")
public class GradeEntryApiController {

    @Autowired
    private AcademicService academicService;

    @Autowired
    private GradeService gradeService;

    @GetMapping("/terms")
    public List<Term> getTermsByAcademicYear(@RequestParam Long AYRID) {
        return academicService.getTermsByAcademicYear(AYRID);
    }

    @GetMapping("/courses")
    public List<CourseDTO> getCoursesByTerm(
            @RequestParam Long TRMID,
            HttpSession session) {
                
        Integer userId = (Integer) session.getAttribute("userid"); // get userid from session

        if (userId == null) {
            throw new RuntimeException("User ID not found in session.");
        }

        return academicService.getCoursesByTermAndFaculty(TRMID, userId.longValue());
    }

    @GetMapping("/exam-types")
    public List<ExamType> getExamTypes(@RequestParam Long CRSID, @RequestParam Long TRMID) {
        return academicService.getExamTypesByCourseAndTerm(CRSID, TRMID);
    }
    
}
