package com.segs.demo.service.impl;

import com.segs.demo.model.Grade;
import com.segs.demo.model.StudentGradeDTO;
import com.segs.demo.repository.GradeRepository;
import com.segs.demo.repository.StudentGradeRepository;
import com.segs.demo.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private StudentGradeRepository studentGradeRepository;

    @Override
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    @Override
    public List<StudentGradeDTO> getStudentGrades(Long CRSID, Long examTypeId, List<String> selectedGrades) {
        List<StudentGradeDTO> allStudentGrades = studentGradeRepository.findByCRSIDAndExamTypeId(CRSID, examTypeId);

        if (selectedGrades == null || selectedGrades.isEmpty()) {
            return allStudentGrades;
        }

        // Filter by selected grades (case sensitive or insensitive depending on requirements)
        return allStudentGrades.stream()
                .filter(sg -> selectedGrades.contains(sg.getGrade()))
                .collect(Collectors.toList());
    }
}
