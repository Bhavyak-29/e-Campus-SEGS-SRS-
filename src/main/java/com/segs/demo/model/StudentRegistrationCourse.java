package com.segs.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "studentregistrationcourses", schema = "ec2")
public class StudentRegistrationCourse {

    @Id
    @Column(name = "srcid", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "srcsrgid", nullable = false)
    private StudentRegistration studentRegistration;

    @ManyToOne
    @JoinColumn(name = "srctcrid", nullable = false)
    private TermCourse termCourse;

    @Column(name = "srctype", nullable = false)
    private String type;

    @Column(name = "srcstatus", nullable = false)
    private String status;

    @Column(name = "srcrowstate", nullable = false)
    private Short rowState;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentRegistration getStudentRegistration() {
        return studentRegistration;
    }

    public void setStudentRegistration(StudentRegistration studentRegistration) {
        this.studentRegistration = studentRegistration;
    }

    public TermCourse getTermCourse() {
        return termCourse;
    }

    public void setTermCourse(TermCourse termCourse) {
        this.termCourse = termCourse;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Short getRowState() {
        return rowState;
    }

    public void setRowState(Short rowState) {
        this.rowState = rowState;
    }

    
}
