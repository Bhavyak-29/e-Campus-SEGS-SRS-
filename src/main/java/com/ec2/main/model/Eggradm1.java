package com.ec2.main.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "eggradm1", schema = "ec2")
public class Eggradm1 {
    @Id
    private Long grad_id;
    private String grad_lt;
    private BigDecimal grad_pt;

    @Column(name = "row_st")
    private short rowstate;


    public short getRowstate() {
        return rowstate;
    }
    public void setRowstate(short rowstate) {
        this.rowstate = rowstate;
    }
    public Long getGrad_id() {
        return grad_id;
    }
    public void setGrad_id(Long grad_id) {
        this.grad_id = grad_id;
    }
    public String getGrad_lt() {
        return grad_lt;
    }
    public void setGrad_lt(String grad_lt) {
        this.grad_lt = grad_lt;
    }
    public Eggradm1() {
    }

    public BigDecimal getGrad_pt() {
        return grad_pt;
    }

    public void setGrad_pt(BigDecimal grad_pt) {
        this.grad_pt = grad_pt;
    }
}
