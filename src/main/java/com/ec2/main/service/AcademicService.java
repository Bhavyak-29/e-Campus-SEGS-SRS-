package com.ec2.main.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ec2.main.model.CourseDTO;
import com.ec2.main.model.ExamType;
import com.ec2.main.model.Terms;
import com.ec2.main.model.TermCourses;
import com.ec2.main.repository.Egcrstt1Repository;
import com.ec2.main.repository.ExamTypeRepository;
import com.ec2.main.repository.TermCoursesRepository;
import com.ec2.main.repository.TermsRepository;

@Service
public class AcademicService {

    @Autowired
    private TermsRepository termRepo;

    @Autowired
    private TermCoursesRepository termCourseRepo;

    @Autowired
    private Egcrstt1Repository egcrstt1Repository;
    
    @Autowired
    private ExamTypeRepository examTypeRepository;


    public List<Terms> getTermsByAcademicYear(Long AYRID) {
        return termRepo.findByAcademicYear_Ayrid(AYRID);
    }

    public List<CourseDTO> getCoursesByTerm(Long TRMID) {
        List<TermCourses> courses = termCourseRepo.findByTerm_Trmid(TRMID);
        return courses.stream().map(tc -> new CourseDTO(tc.getCourse().getCrsid(), tc.getCourse().getCrscode(), tc.getCourse().getCrsname())).toList();
    }

    public List<CourseDTO> getCoursesByTermAndFaculty(Long TRMID, Long userId) {
        
        List<TermCourses> courses = termCourseRepo.findByTerm_TrmidAndUser_Uid(TRMID, userId);
            return courses.stream()
                .map(tc -> new CourseDTO(tc.getCourse().getCrsid(), tc.getCourse().getCrscode(), tc.getCourse().getCrsname()))
                .toList();
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
