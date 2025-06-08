package com.segs.demo.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "egcrstt1", schema = "ec2")
public class ExamResult {

    @Id
    @Column(name = "EXMRID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "examtype_id")
    private ExamType examType;

    @ManyToOne
    @JoinColumn(name = "tcrid")
    private TermCourse termCourse;

    @Column(name = "row_st")
    private Integer rowState;

    @ManyToOne
    @JoinColumn(name = "CRSID")
    private Course course;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExamType getExamType() {
        return examType;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
    }

    public TermCourse getTermCourse() {
        return termCourse;
    }

    public void setTermCourse(TermCourse termCourse) {
        this.termCourse = termCourse;
    }

    public Integer getRowState() {
        return rowState;
    }

    public void setRowState(Integer rowState) {
        this.rowState = rowState;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
    
}

