package com.segs.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.segs.demo.model.CourseDTO;
import com.segs.demo.model.ExamResult;
import com.segs.demo.model.ExamType;
import com.segs.demo.model.Term;
import com.segs.demo.model.TermCourse;
import com.segs.demo.repository.ExamResultRepository;
import com.segs.demo.repository.TermCourseRepository;
import com.segs.demo.repository.TermRepository;

@Service
public class AcademicService {

    @Autowired
    private TermRepository termRepo;

    @Autowired
    private TermCourseRepository termCourseRepo;

    @Autowired
    private ExamResultRepository examResultRepo;

    // @Autowired
    // private ExamTypeRepository examTypeRepo;

    public List<Term> getTermsByAcademicYear(Long AYRID) {
        return termRepo.findByAcademicYear_Id(AYRID);
    }

    public List<CourseDTO> getCoursesByTerm(Long TRMID) {
        List<TermCourse> courses = termCourseRepo.findByTerm_Id(TRMID);
        return courses.stream().map(tc -> new CourseDTO(tc.getCourse().getId(), tc.getCourse().getCode(), tc.getCourse().getName())).toList();
    }

    public List<ExamType> getExamTypesByCourse(Long CRSID) {
        List<ExamResult> results = examResultRepo.findByTermCourse_Course_Id(CRSID);
        return results.stream()
            .map(r -> r.getExamType())
            .distinct()
            .toList();
    }
}
