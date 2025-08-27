package com.ec2.main.model;

import java.util.ArrayList;
import java.util.List;

public class StudentGradeDTOWrapper {
    private List<StudentGradeDTO> gradesList= new ArrayList<>();;
    public List<StudentGradeDTO> getGradesList() {
        return gradesList;
    }
    public void setGradesList(List<StudentGradeDTO> gradesList) {
        this.gradesList = gradesList != null ? gradesList : new ArrayList<>();
    }
}
