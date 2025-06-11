package com.segs.demo.model;

public class StudentGradeDTO {
    private String studentId;
    private String studentName;
    private String studentEmail;
    private String grade;
    private boolean selectedForUpdate;

    public StudentGradeDTO() {}
    public StudentGradeDTO(String studentId, String studentName, String studentEmail, String grade) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.grade = grade;
    }

    // Getters and setters

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public boolean isSelectedForUpdate() {
        return selectedForUpdate;
    }

    public void setSelectedForUpdate(boolean selectedForUpdate) {
        this.selectedForUpdate = selectedForUpdate;
    }
}
