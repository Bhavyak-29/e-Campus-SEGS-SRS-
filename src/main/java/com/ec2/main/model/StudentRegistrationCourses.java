package com.ec2.main.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "studentregistrationcourses", schema="ec2")
public class StudentRegistrationCourses {

    @Id
    @Column(name = "srcid")
    private Long srcid;

    @Column(name = "srcsrgid")
    private Long srcsrgid;

    @Column(name = "srctcrid")
    private Long srctcrid;

    @Column(name = "srctype")
    private String srctype;

    @Column(name = "srcscrid")
    private Integer srcscrid;

    @Column(name = "srcstatus")
    private String srcstatus;

    @Column(name = "srcfield1")
    private String srcfield1;

    @Column(name = "srcfield2")
    private String srcfield2;

    @Column(name = "srcfield3")
    private String srcfield3;

    @Column(name = "srccreatedby")
    private Long srccreatedby;

    @Column(name = "srccreatedat")
    private LocalDateTime srccreatedat;

    @Column(name = "srclastupdatedby")
    private Long srclastupdatedby;

    @Column(name = "srclastupdatedat")
    private LocalDateTime srclastupdatedat;

    @Column(name = "srcrowstate")
    private Long srcrowstate;


    public Long getSrcid() { return srcid; }
    public void setSrcid(Long srcid) { this.srcid = srcid; }

    public Long getSrcsrgid() { return srcsrgid; }
    public void setSrcsrgid(Long srcsrgid) { this.srcsrgid = srcsrgid; }

    public Long getSrctcrid() { return srctcrid; }
    public void setSrctcrid(Long srctcrid) { this.srctcrid = srctcrid; }

    public String getSrctype() { return srctype; }
    public void setSrctype(String srctype) { this.srctype = srctype; }

    public Integer getSrcscrid() { return srcscrid; }
    public void setSrcscrid(Integer srcscrid) { this.srcscrid = srcscrid; }

    public String getSrcstatus() { return srcstatus; }
    public void setSrcstatus(String srcstatus) { this.srcstatus = srcstatus; }

    public String getSrcfield1() { return srcfield1; }
    public void setSrcfield1(String srcfield1) { this.srcfield1 = srcfield1; }

    public String getSrcfield2() { return srcfield2; }
    public void setSrcfield2(String srcfield2) { this.srcfield2 = srcfield2; }

    public String getSrcfield3() { return srcfield3; }
    public void setSrcfield3(String srcfield3) { this.srcfield3 = srcfield3; }

    public Long getSrccreatedby() { return srccreatedby; }
    public void setSrccreatedby(Long srccreatedby) { this.srccreatedby = srccreatedby; }

    public LocalDateTime getSrccreatedat() { return srccreatedat; }
    public void setSrccreatedat(LocalDateTime srccreatedat) { this.srccreatedat = srccreatedat; }

    public Long getSrclastupdatedby() { return srclastupdatedby; }
    public void setSrclastupdatedby(Long srclastupdatedby) { this.srclastupdatedby = srclastupdatedby; }

    public LocalDateTime getSrclastupdatedat() { return srclastupdatedat; }
    public void setSrclastupdatedat(LocalDateTime srclastupdatedat) { this.srclastupdatedat = srclastupdatedat; }

    public Long getSrcrowstate() { return srcrowstate; }
    public void setSrcrowstate(Long srcrowstate) { this.srcrowstate = srcrowstate; }

}
