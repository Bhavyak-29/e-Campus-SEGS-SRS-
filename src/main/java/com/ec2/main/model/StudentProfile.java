package com.ec2.main.model;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "students", schema = "ec2")
public class StudentProfile {

    @Id
    @Column(name = "stdid")
    private Long stdid;

    @Column(name = "stddob")
    private Date stddob;

    @Column(name = "stdgender")
    private String stdgender;

    @Column(name = "stdplustwo")
    private BigDecimal stdplustwo;

    @Column(name = "stdplustwoboard")
    private String stdplustwoboard;

    @Column(name = "stdplustwoyear")
    private String stdplustwoyear;

    @Column(name = "stdheight")
    private BigDecimal stdheight;

    @Column(name = "stdidmark")
    private String stdidmark;

    @Column(name = "stdbldgrp")
    private String stdbldgrp;

    @Column(name = "stdphotolocation")
    private String stdphotolocation;

    // Optional: Address fields if needed (can be mapped as @ManyToOne or kept as Longs)
    @Column(name = "stdcurradrid")
    private Long stdcurradrid;

    @Column(name = "stdprmtadrid")
    private Long stdprmtadrid;

    @Column(name = "stdemrgadrid")
    private Long stdemrgadrid;

    // Getters & setters
    public Long getStdid() { return stdid; }
    public void setStdid(Long stdid) { this.stdid = stdid; }

    public Date getStddob() { return stddob; }
    public void setStddob(Date stddob) { this.stddob = stddob; }

    public String getStdgender() { return stdgender; }
    public void setStdgender(String stdgender) { this.stdgender = stdgender; }

    public BigDecimal getStdplustwo() { return stdplustwo; }
    public void setStdplustwo(BigDecimal stdplustwo) { this.stdplustwo = stdplustwo; }

    public String getStdplustwoboard() { return stdplustwoboard; }
    public void setStdplustwoboard(String stdplustwoboard) { this.stdplustwoboard = stdplustwoboard; }

    public String getStdplustwoyear() { return stdplustwoyear; }
    public void setStdplustwoyear(String stdplustwoyear) { this.stdplustwoyear = stdplustwoyear; }

    public BigDecimal getStdheight() { return stdheight; }
    public void setStdheight(BigDecimal stdheight) { this.stdheight = stdheight; }

    public String getStdidmark() { return stdidmark; }
    public void setStdidmark(String stdidmark) { this.stdidmark = stdidmark; }

    public String getStdbldgrp() { return stdbldgrp; }
    public void setStdbldgrp(String stdbldgrp) { this.stdbldgrp = stdbldgrp; }

    public String getStdphotolocation() { return stdphotolocation; }
    public void setStdphotolocation(String stdphotolocation) { this.stdphotolocation = stdphotolocation; }

    public Long getStdcurradrid() { return stdcurradrid; }
    public void setStdcurradrid(Long stdcurradrid) { this.stdcurradrid = stdcurradrid; }

    public Long getStdprmtadrid() { return stdprmtadrid; }
    public void setStdprmtadrid(Long stdprmtadrid) { this.stdprmtadrid = stdprmtadrid; }

    public Long getStdemrgadrid() { return stdemrgadrid; }
    public void setStdemrgadrid(Long stdemrgadrid) { this.stdemrgadrid = stdemrgadrid; }
}
