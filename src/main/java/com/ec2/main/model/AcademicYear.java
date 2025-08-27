package com.ec2.main.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "academicyears", schema = "ec2")
public class AcademicYear {
    @Id
    @Column(name = "ayrid")
    private Long id;

    @Column(name = "ayrname")
    private String name;

    @Column(name = "ayrrowstate")
    private Integer rowState;

    @Column(name = "ayrfield1")
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
