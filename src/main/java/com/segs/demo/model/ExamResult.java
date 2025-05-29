package com.segs.demo.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "EGGRSTT1")
public class ExamResult {

    @Id
    @Column(name = "EXMRID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "EXAMTYPE_ID")
    private ExamType examType;

    @ManyToOne
    @JoinColumn(name = "TCRID")
    private TermCourse termCourse;

    @Column(name = "ROW_ST")
    private Integer rowState;

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
    
}

