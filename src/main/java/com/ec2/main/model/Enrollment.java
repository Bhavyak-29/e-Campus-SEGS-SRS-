package com.ec2.main.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ENROLLMENTS")
public class Enrollment {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ENRID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENTID", nullable = false)
    private Students student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CRSID", nullable = false)
    private Courses course;

    @Column(name = "ENRROWSTATE")
    private Integer rowState;

    @Column(name = "ENRSTATUS")
    private String status; // e.g., enrolled, dropped, completed

    @Column(name = "ENRMARKS")
    private Double marks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRMID", nullable = false) // Adjust column name as per your DB schema
    private Terms term;

    
    
    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Students getStudent() {
        return student;
    }

    public void setStudent(Students student) {
        this.student = student;
    }

    public Courses getCourse() {
        return course;
    }

    public void setCourse(Courses course) {
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

    public Terms getTerm() {
        return term;
    }

    public void setTerm(Terms term) {
        this.term = term;
    }
}
