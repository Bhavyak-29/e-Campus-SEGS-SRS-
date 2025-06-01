package com.segs.demo.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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

    @ManyToOne
    @JoinColumn(name = "TCRFACULTYID")
    private Users user;

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

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    // public Integer getFacultyId() {
    //     return facultyId;
    // }

    // public void setFacultyId(Integer facultyId) {
    //     this.facultyId = facultyId;
    // }

    public Integer getRowState() {
        return rowState;
    }

    public void setRowState(Integer rowState) {
        this.rowState = rowState;
    }

    
}

