package com.segs.demo.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.segs.demo.model.CourseDTO;
import com.segs.demo.model.ExamType;
import com.segs.demo.model.Term;
import com.segs.demo.model.TermCourse;
import com.segs.demo.repository.Egcrstt1Repository;
import com.segs.demo.repository.ExamTypeRepository;
import com.segs.demo.repository.TermCourseRepository;
import com.segs.demo.repository.TermRepository;

@Service
public class AcademicService {

    @Autowired
    private TermRepository termRepo;

    @Autowired
    private TermCourseRepository termCourseRepo;

    @Autowired
    private Egcrstt1Repository egcrstt1Repository;
    
    @Autowired
    private ExamTypeRepository examTypeRepository;


    public List<Term> getTermsByAcademicYear(Long AYRID) {
        return termRepo.findByAcademicYear_Id(AYRID);
    }

    public List<CourseDTO> getCoursesByTerm(Long TRMID) {
        List<TermCourse> courses = termCourseRepo.findByTerm_Id(TRMID);
        return courses.stream().map(tc -> new CourseDTO(tc.getCourse().getId(), tc.getCourse().getCode(), tc.getCourse().getName())).toList();
    }

    public List<ExamType> getExamTypesByCourseAndTerm(Long crsid, Long trmid) {
        Long tcrid = termCourseRepo.findTcridByCrsidAndTrmid(crsid, trmid);
        if (tcrid == null) {
            return Collections.emptyList();
        }
        List<Long> examTypeIds = egcrstt1Repository.findDistinctExamTypeIdsByTcrid(tcrid);
        if (examTypeIds.isEmpty()) {
            return Collections.emptyList();
        }
        return examTypeRepository.findByIdIn(examTypeIds);
    }
}
