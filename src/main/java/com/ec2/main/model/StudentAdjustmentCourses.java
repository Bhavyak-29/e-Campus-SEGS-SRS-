package com.ec2.main.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "studentadjustmentcourses", schema="ec2")
public class StudentAdjustmentCourses {

    @Id
    @Column(name = "sacid")
    private Long sacid;

    @Column(name = "sacsrcid")
    private Long sacsrcid;

    @Column(name = "sacsrgid")
    private Long sacsrgid;

    @Column(name = "sactcrid")
    private Integer sactcrid;

    @Column(name = "sactype")
    private String sactype;

    @Column(name = "sacscrid")
    private Integer sacscrid;

    @Column(name = "sacstatus")
    private String sacstatus;

    @Column(name = "sacfield1")
    private String sacfield1;

    @Column(name = "sacfield2")
    private String sacfield2;

    @Column(name = "saccreatedby")
    private Long saccreatedby;

    @Column(name = "saccreatedat")
    private LocalDateTime saccreatedat;

    @Column(name = "saclastupdatedby")
    private Long saclastupdatedby;

    @Column(name = "saclastupdatedat")
    private LocalDateTime saclastupdatedat;

    @Column(name = "sacrowstate")
    private Integer sacrowstate;


    public Long getSacid() { return sacid; }
    public void setSacid(Long sacid) { this.sacid = sacid; }

    public Long getSacsrcid() { return sacsrcid; }
    public void setSacsrcid(Long sacsrcid) { this.sacsrcid = sacsrcid; }

    public Long getSacsrgid() { return sacsrgid; }
    public void setSacsrgid(Long sacsrgid) { this.sacsrgid = sacsrgid; }

    public Integer getSactcrid() { return sactcrid; }
    public void setSactcrid(Integer sactcrid) { this.sactcrid = sactcrid; }

    public String getSactype() { return sactype; }
    public void setSactype(String sactype) { this.sactype = sactype; }

    public Integer getSacscrid() { return sacscrid; }
    public void setSacscrid(Integer sacscrid) { this.sacscrid = sacscrid; }

    public String getSacstatus() { return sacstatus; }
    public void setSacstatus(String sacstatus) { this.sacstatus = sacstatus; }

    public String getSacfield1() { return sacfield1; }
    public void setSacfield1(String sacfield1) { this.sacfield1 = sacfield1; }

    public String getSacfield2() { return sacfield2; }
    public void setSacfield2(String sacfield2) { this.sacfield2 = sacfield2; }

    public Long getSaccreatedby() { return saccreatedby; }
    public void setSaccreatedby(Long saccreatedby) { this.saccreatedby = saccreatedby; }

    public LocalDateTime getSaccreatedat() { return saccreatedat; }
    public void setSaccreatedat(LocalDateTime saccreatedat) { this.saccreatedat = saccreatedat; }

    public Long getSaclastupdatedby() { return saclastupdatedby; }
    public void setSaclastupdatedby(Long saclastupdatedby) { this.saclastupdatedby = saclastupdatedby; }

    public LocalDateTime getSaclastupdatedat() { return saclastupdatedat; }
    public void setSaclastupdatedat(LocalDateTime saclastupdatedat) { this.saclastupdatedat = saclastupdatedat; }

    public Integer getSacrowstate() { return sacrowstate; }
    public void setSacrowstate(Integer sacrowstate) { this.sacrowstate = sacrowstate; }

}
