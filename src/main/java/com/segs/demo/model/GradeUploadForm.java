package com.segs.demo.model;

import jakarta.validation.constraints.NotBlank;

public class GradeUploadForm {

    @NotBlank
    private String term;

    @NotBlank
    private String course;

    @NotBlank
    private Long examtype;

    @NotBlank
    private String studentId;

    @NotBlank
    private String grade;

    // Getters and Setters

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Long getExamtype() {
        return examtype;
    }

    public void setExamtype(Long examtype) {
        this.examtype = examtype;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}

