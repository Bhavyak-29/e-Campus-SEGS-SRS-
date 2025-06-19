package com.segs.demo.model;

public class StudentGradeReportDTO {
    private String studentInstituteId;
    private String studentName;
    private String obtainedGrade;

    public StudentGradeReportDTO(String studentInstituteId, String studentName, String obtainedGrade) {
        this.studentInstituteId = studentInstituteId;
        this.studentName = studentName;
        this.obtainedGrade = obtainedGrade;
    }

    // Getters
    public String getStudentInstituteId() {
        return studentInstituteId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getObtainedGrade() {
        return obtainedGrade;
    }
}