package com.segs.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ENROLLMENTS")
public class Enrollment {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ENRID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENTID", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CRSID", nullable = false)
    private Course course;

    @Column(name = "ENRROWSTATE")
    private Integer rowState;

    @Column(name = "ENRSTATUS")
    private String status; // e.g., enrolled, dropped, completed

    @Column(name = "ENRMARKS")
    private Double marks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRMID", nullable = false) // Adjust column name as per your DB schema
    private Term term;

    
    
    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Integer getRowState() {
        return rowState;
    }

    public void setRowState(Integer rowState) {
        this.rowState = rowState;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getMarks() {
        return marks;
    }

    public void setMarks(Double marks) {
        this.marks = marks;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }
}
