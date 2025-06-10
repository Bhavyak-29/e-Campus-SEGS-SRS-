package com.segs.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "termcoursecredits", schema = "ec2")
public class TermCourseCredit {

    @Id
    @Column(name = "tccid")
    private Short id;

    @ManyToOne
    @JoinColumn(name = "tcctcrid", nullable = false)
    private TermCourse termCourse;

    @Column(name = "tcclectures", nullable = false)
    private BigDecimal lectures;

    @Column(name = "tcctutorials", nullable = false)
    private BigDecimal tutorials;

    @Column(name = "tccpracticals", nullable = false)
    private BigDecimal practicals;

    @Column(name = "tcccreditpoints", nullable = false)
    private BigDecimal creditPoints;

    @Column(name = "tccmarks", nullable = false)
    private Short marks;

    @Column(name = "tcccreatedby")
    private Long createdBy;

    @Column(name = "tcccreatedat", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "tcclastupdatedby")
    private Long lastUpdatedBy;

    @Column(name = "tcclastupdatedat")
    private LocalDateTime lastUpdatedAt;

    @Column(name = "tccrowstate", nullable = false)
    private Short rowState;

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public TermCourse getTermCourse() {
        return termCourse;
    }

    public void setTermCourse(TermCourse termCourse) {
        this.termCourse = termCourse;
    }

    public BigDecimal getLectures() {
        return lectures;
    }

    public void setLectures(BigDecimal lectures) {
        this.lectures = lectures;
    }

    public BigDecimal getTutorials() {
        return tutorials;
    }

    public void setTutorials(BigDecimal tutorials) {
        this.tutorials = tutorials;
    }

    public BigDecimal getPracticals() {
        return practicals;
    }

    public void setPracticals(BigDecimal practicals) {
        this.practicals = practicals;
    }

    public BigDecimal getCreditPoints() {
        return creditPoints;
    }

    public void setCreditPoints(BigDecimal creditPoints) {
        this.creditPoints = creditPoints;
    }

    public Short getMarks() {
        return marks;
    }

    public void setMarks(Short marks) {
        this.marks = marks;
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
        return rowState;
    }

    public void setRowState(Short rowState) {
        this.rowState = rowState;
    }
}

