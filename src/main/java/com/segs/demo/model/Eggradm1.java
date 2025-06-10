package com.segs.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "eggradm1", schema = "ec2")
public class Eggradm1 {
    @Id
    private Long grad_id;
    private String grad_lt;


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
}
