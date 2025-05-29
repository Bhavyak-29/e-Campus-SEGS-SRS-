package com.segs.demo.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "COURSES")
public class Course {
    @Id
    @Column(name = "CRSID")
    private Long id;

    @Column(name = "CRSNAME")
    private String name;

    @Column(name = "CRSCODE")
    private String code;

    @Column(name = "CRSROWSTATE")
    private Integer rowState;

    @ManyToOne
    @JoinColumn(name = "TRMID")  // this column references TERM.TRMID
    private Term term;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getRowState() {
        return rowState;
    }

    public void setRowState(Integer rowState) {
        this.rowState = rowState;
    }    

    public Term getTerm() { 
        return term; 
    }
    
    public void setTerm(Term term) { 
        this.term = term; 
    }
}

