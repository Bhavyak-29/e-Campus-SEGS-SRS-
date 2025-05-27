package com.segs.demo.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
@Entity
@Table(name = "ACADEMICYEARS")
public class AcademicYear {
    @Id
    @Column(name = "AYRID")
    private Long id;

    @Column(name = "AYRNAME")
    private String name;

    @Column(name = "AYRROWSTATE")
    private Integer rowState;

    @Column(name = "AYRFIELD1")
    private Integer field1;

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

    public Integer getRowState() {
        return rowState;
    }

    public void setRowState(Integer rowState) {
        this.rowState = rowState;
    }

    public Integer getField1() {
        return field1;
    }

    public void setField1(Integer field1) {
        this.field1 = field1;
    }

    
}
