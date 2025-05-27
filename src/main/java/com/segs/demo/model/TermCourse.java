package com.segs.demo.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "TERMCOURSES")
public class TermCourse {
    @Id
    @Column(name = "TCRID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TCRTRMID")
    private Term term;

    @ManyToOne
    @JoinColumn(name = "TCRCRSID")
    private Course course;

    @Column(name = "TCRFACULTYID")
    private String facultyId;

    @Column(name = "TCRROWSTATE")
    private Integer rowState;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public Integer getRowState() {
        return rowState;
    }

    public void setRowState(Integer rowState) {
        this.rowState = rowState;
    }

    
}

