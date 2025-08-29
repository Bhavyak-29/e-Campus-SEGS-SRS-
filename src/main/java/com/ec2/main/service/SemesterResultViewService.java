package com.ec2.main.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ec2.main.repository.*;
import com.ec2.main.model.*;


@Service
public class SemesterResultViewService {
    @Autowired
    private Egcrstt1Repository egcrstt1Repo;

    @Autowired
    private Eggradm1Repository eggradm1Repo;

    @Autowired
    private StudentSemesterResultRepository ssrRepo;

    public Egcrstt1 getObtgrByTcridAndStdid(Long studentId, Long tcrid) {
        return egcrstt1Repo.getObtgrId(studentId, tcrid);
    }

    public String getGradeByObtgr(Long obtgr) {
        return eggradm1Repo.getGrade(obtgr);
    }

    public StudentSemesterResult getSsrBySrg(Long srgid) {
        return ssrRepo.getBySrgid(srgid);
    }
    
}
