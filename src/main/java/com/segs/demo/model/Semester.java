package com.segs.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "semesters",schema="ec2")
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long strid;

    private int strrowstate;
    private int strseqno;
    
    public Long getStrid() {
        return strid;
    }
    public void setStrid(Long strid) {
        this.strid = strid;
    }
    public int getStrrowstate() {
        return strrowstate;
    }
    public void setStrrowstate(int strrowstate) {
        this.strrowstate = strrowstate;
    }
    public int getStrseqno() {
        return strseqno;
    }
    public void setStrseqno(int strseqno) {
        this.strseqno = strseqno;
    }

    
}
