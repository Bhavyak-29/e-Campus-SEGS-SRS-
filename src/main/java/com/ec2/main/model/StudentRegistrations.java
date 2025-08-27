package com.ec2.main.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "studentregistrations", schema = "ec2")
public class StudentRegistrations {

    @Id
    @Column(name = "srgid", nullable = false)
    private Long srgid;

    @ManyToOne
    @JoinColumn(name = "srgstdid", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "srgstrid", nullable = false)
    private Semester semester;

    @Column(name = "srgregdate", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "srgfield1")
    private String field1;

    @Column(name = "srgfield2")
    private String field2;

    @Column(name = "srgcreatedby")
    private Long createdBy;

    @Column(name = "srgcreatedat", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "srglastupdatedby")
    private Long lastUpdatedBy;

    @Column(name = "srglastupdatedat")
    private LocalDateTime lastUpdatedAt;

    @Column(name = "srgrowstate", nullable = false)
    private Short srgrowstate;

    public Long getSrgid() {
        return srgid;
    }
    public void setSrgid(Long srgid) {
        this.srgid = srgid;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public Short getRowState() {
        return srgrowstate;
    }

    public void setRowState(Short rowState) {
        this.srgrowstate = rowState;
    }

    
}
