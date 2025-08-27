package com.ec2.main.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "semesters", schema = "ec2")
public class Semester {

    @Id
    @Column(name = "strid")
    private Long strid; // Should not be @GeneratedValue if the ID is assigned manually

    // --- Relationships based on Foreign Keys ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "strbchid", referencedColumnName = "bchid")
    @JsonIgnore
    private Batch batch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "strtrmid", referencedColumnName = "trmid")
    @JsonIgnore
    private Term term; // Assuming you have a 'Term' entity

    // --- Other columns from your table ---

    @Column(name = "strname", nullable = false)
    private String strname;

    @Column(name = "strrowstate", nullable = false)
    private int strrowstate;

    @Column(name = "strseqno")
    private int strseqno;

    @Column(name = "strcreatedat", nullable = false)
    private LocalDateTime strcreatedat;

    @Column(name = "strlastupdatedat")
    private LocalDateTime strlastupdatedat;
    
    @Column(name = "strresultdecdate")
    private LocalDateTime strresultdecdate;
    
    @Column(name = "strregstatus")
    private String strregstatus;

    @Column(name = "stradddropstatus")
    private String stradddropstatus;

    // Getters and Setters for all fields

    public Long getStrid() {
        return strid;
    }

    public void setStrid(Long strid) {
        this.strid = strid;
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public String getStrname() {
        return strname;
    }

    public void setStrname(String strname) {
        this.strname = strname;
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

    public LocalDateTime getStrcreatedat() {
        return strcreatedat;
    }

    public void setStrcreatedat(LocalDateTime strcreatedat) {
        this.strcreatedat = strcreatedat;
    }

    public LocalDateTime getStrlastupdatedat() {
        return strlastupdatedat;
    }

    public void setStrlastupdatedat(LocalDateTime strlastupdatedat) {
        this.strlastupdatedat = strlastupdatedat;
    }

    public LocalDateTime getStrresultdecdate() {
        return strresultdecdate;
    }

    public void setStrresultdecdate(LocalDateTime strresultdecdate) {
        this.strresultdecdate = strresultdecdate;
    }

    public String getStrregstatus() {
        return strregstatus;
    }

    public void setStrregstatus(String strregstatus) {
        this.strregstatus = strregstatus;
    }

    public String getStradddropstatus() {
        return stradddropstatus;
    }

    public void setStradddropstatus(String stradddropstatus) {
        this.stradddropstatus = stradddropstatus;
    }
}