package com.segs.demo.model;

public class StudentGradeDTO {
    private Long studentId;
    private String studentName;
    private String studentEmail;
    private String grade;

    public StudentGradeDTO(Long studentId, String studentName, String studentEmail, String grade) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.grade = grade;
    }

    // Getters and setters

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
}
