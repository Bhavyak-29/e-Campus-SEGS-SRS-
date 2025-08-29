package com.ec2.main.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import com.ec2.main.model.*;
import com.ec2.main.repository.*;

@Service
public class CourseDropEditService {
    @Autowired
    private StudentAdjustmentCoursesRepository studentAdjustmentRepo;

    @Autowired
    private StudentRegistrationCoursesRepository studentRegistrationCoursesRepo;

    public void findStudentAdjustmentCourses(Long srgid, Long studentId) {
        List<StudentAdjustmentCourses> studentAdjustmentCourses = studentAdjustmentRepo.findBySrgid(srgid);
        if(studentAdjustmentCourses.size()==0 || studentAdjustmentCourses.isEmpty()) {

            List<StudentRegistrationCourses> studentRegistrationCourses = studentRegistrationCoursesRepo.findBySrgid(srgid);

            for( StudentRegistrationCourses src : studentRegistrationCourses) {
                
                Optional<Long> maxsacid = studentAdjustmentRepo.getMaxId();
                StudentAdjustmentCourses sac = new StudentAdjustmentCourses();
                sac.setSacid(maxsacid.orElse(0L)+1);
                sac.setSacsrcid(src.getSrcid());
                sac.setSacsrgid(srgid);
                sac.setSactcrid(src.getSrctcrid().intValue());
                sac.setSactype(src.getSrctype());
                sac.setSacscrid(src.getSrcscrid());
                sac.setSacstatus(src.getSrcstatus());
                sac.setSaccreatedby(studentId);
                sac.setSaccreatedat(LocalDateTime.now());
                sac.setSacrowstate(1);
                studentAdjustmentRepo.save(sac);
            }
        }
    }
}
