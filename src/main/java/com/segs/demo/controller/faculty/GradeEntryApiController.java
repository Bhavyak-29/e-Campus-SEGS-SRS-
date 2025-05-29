package com.segs.demo.controller.faculty;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.segs.demo.model.CourseDTO;
import com.segs.demo.model.ExamType;
import com.segs.demo.model.Term;
import com.segs.demo.service.AcademicService;



@RestController
@RequestMapping("/api")
public class GradeEntryApiController {

    @Autowired
    private AcademicService academicService;

    @GetMapping("/terms")
    public List<Term> getTermsByAcademicYear(@RequestParam Long AYRID) {
        return academicService.getTermsByAcademicYear(AYRID);
    }

    @GetMapping("/courses")
    public List<CourseDTO> getCoursesByTerm(@RequestParam Long TRMID) {
        return academicService.getCoursesByTerm(TRMID);
    }

    @GetMapping("/exam-types")
    public List<ExamType> getExamTypesByCourse(@RequestParam Long CRSID) {
        return academicService.getExamTypesByCourse(CRSID);
    }
}
