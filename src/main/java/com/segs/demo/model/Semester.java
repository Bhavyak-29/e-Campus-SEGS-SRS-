package com.segs.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "SEMESTERS")
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
