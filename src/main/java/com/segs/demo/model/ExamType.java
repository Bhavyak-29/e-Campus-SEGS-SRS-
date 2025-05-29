package com.segs.demo.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "EGEXAMM1")
public class ExamType {
    @Id
    @Column(name = "EXAMTYPE_ID")
    private Long id;

    @Column(name = "EXAMTYPE_TITLE")
    private String title;

    @Column(name = "ROW_ST")
    private Integer rowState;

    @ManyToOne
    @JoinColumn(name = "CRSID") // ← replace with actual column name in table
    private Course course;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
