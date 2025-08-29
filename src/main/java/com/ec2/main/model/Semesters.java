package com.ec2.main.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "semesters", schema="ec2")
public class Semesters {

    @Id
    @Column(name = "strid")
    private Long strid;

    @Column(name = "strcalid")
    private Long strcalid;

    @Column(name = "strname")
    private String strname;

    @Column(name = "strfield1")
    private String strfield1;

    @Column(name = "strfield2")
    private String strfield2;

    @Column(name = "strfield3")
    private String strfield3;

    @Column(name = "strcreatedby")
    private Long strcreatedby;

    @Column(name = "strcreatedat")
    private LocalDateTime strcreatedat;

    @Column(name = "strlastupdatedby")
    private Long strlastupdatedby;

    @Column(name = "strlastupdatedat")
    private LocalDateTime strlastupdatedat;

    @Column(name = "strrowstate")
    private Long strrowstate;

    @Column(name = "strseqno")
    private Long strseqno;

    @Column(name = "strstcid")
    private Long strstcid;

    @Column(name = "strresultdecdate")
    private LocalDateTime strresultdecdate;

    @Column(name = "strregstatus")
    private String strregstatus;

    @Column(name = "stradddropstatus")
    private String stradddropstatus;

    @Column(name = "strbchid", insertable = false, updatable = false)
    private Long strbchid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "strbchid", referencedColumnName = "bchid", nullable = false)
    @JsonIgnore
    private Batches batches;

    @Column(name = "strtrmid", insertable = false, updatable = false)
    private Long strtrmid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "strtrmid", referencedColumnName = "trmid", nullable = false)
    @JsonIgnore
    private Terms terms;



    public Batches getBatches() {
        return batches;
    }
    public void setBatches(Batches batches) {
        this.batches = batches;
    }
    public Terms getTerms() {
        return terms;
    }
    public void setTerms(Terms terms) {
        this.terms = terms;
    }
    public Long getStrid() { return strid; }
    public void setStrid(Long strid) { this.strid = strid; }

    public Long getStrbchid() { return strbchid; }
    public void setStrbchid(Long strbchid) { this.strbchid = strbchid; }

    public Long getStrtrmid() { return strtrmid; }
    public void setStrtrmid(Long strtrmid) { this.strtrmid = strtrmid; }

    public Long getStrcalid() { return strcalid; }
    public void setStrcalid(Long strcalid) { this.strcalid = strcalid; }

    public String getStrname() { return strname; }
    public void setStrname(String strname) { this.strname = strname; }

    public String getStrfield1() { return strfield1; }
    public void setStrfield1(String strfield1) { this.strfield1 = strfield1; }

    public String getStrfield2() { return strfield2; }
    public void setStrfield2(String strfield2) { this.strfield2 = strfield2; }

    public String getStrfield3() { return strfield3; }
    public void setStrfield3(String strfield3) { this.strfield3 = strfield3; }

    public Long getStrcreatedby() { return strcreatedby; }
    public void setStrcreatedby(Long strcreatedby) { this.strcreatedby = strcreatedby; }

    public LocalDateTime getStrcreatedat() { return strcreatedat; }
    public void setStrcreatedat(LocalDateTime strcreatedat) { this.strcreatedat = strcreatedat; }

    public Long getStrlastupdatedby() { return strlastupdatedby; }
    public void setStrlastupdatedby(Long strlastupdatedby) { this.strlastupdatedby = strlastupdatedby; }

    public LocalDateTime getStrlastupdatedat() { return strlastupdatedat; }
    public void setStrlastupdatedat(LocalDateTime strlastupdatedat) { this.strlastupdatedat = strlastupdatedat; }

    public Long getStrrowstate() { return strrowstate; }
    public void setStrrowstate(Long strrowstate) { this.strrowstate = strrowstate; }

    public Long getStrseqno() { return strseqno; }
    public void setStrseqno(Long strseqno) { this.strseqno = strseqno; }

    public Long getStrstcid() { return strstcid; }
    public void setStrstcid(Long strstcid) { this.strstcid = strstcid; }

    public LocalDateTime getStrresultdecdate() { return strresultdecdate; }
    public void setStrresultdecdate(LocalDateTime strresultdecdate) { this.strresultdecdate = strresultdecdate; }

    public String getStrregstatus() { return strregstatus; }
    public void setStrregstatus(String strregstatus) { this.strregstatus = strregstatus; }

    public String getStradddropstatus() { return stradddropstatus; }
    public void setStradddropstatus(String stradddropstatus) { this.stradddropstatus = stradddropstatus; }

}
