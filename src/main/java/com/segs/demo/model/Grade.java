package com.segs.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "GRADES")
public class Grade {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GRADEID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENRID", nullable = false)
    private Enrollment enrollment;

    @Column(name = "GRADE_VALUE", length = 5)
    private String gradeValue; // e.g., A+, B, C, etc.

    @Column(name = "GRADE_REMARKS", length = 255)
    private String remarks;

    @Column(name = "ROWSTATE")
    private Integer rowState;

    // Changed examType from String to Long for easier relation or ID reference
    @Column(name = "EXAM_TYPE")
    private Long examType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACULTY_ID")
    private Users faculty;

    public Users getFaculty() {
        return faculty;
    }

    public void setFaculty(Users faculty) {
        this.faculty = faculty;
    }

    // Constructors
    public Grade() {}

    public Grade(Enrollment enrollment, String gradeValue, Long examType, Course course, Term term, String remarks, Integer rowState) {
        this.enrollment = enrollment;
        this.gradeValue = gradeValue;
        this.examType = examType;
        this.remarks = remarks;
        this.rowState = rowState;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public String getGradeValue() {
        return gradeValue;
    }

    public void setGradeValue(String gradeValue) {
        this.gradeValue = gradeValue;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getRowState() {
        return rowState;
    }

    public void setRowState(Integer rowState) {
        this.rowState = rowState;
    }

    public Long getExamType() {
        return examType;
    }

    public void setExamType(Long examType) {
        this.examType = examType;
    }
}
