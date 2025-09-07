package com.ec2.main.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "termcourseavailablefor", schema="ec2")
public class TermCourseAvailableFor {

    @Id
    @Column(name = "tcaid")
    private Long tcaid;

    @Column(name = "tcatcrid")
    private Long tcatcrid;

    @Column(name = "tcaprgid")
    private Long tcaprgid;

    @Column(name = "tcastatus")
    private String tcastatus;

    @Column(name = "tcacreatedby")
    private Long tcacreatedby;

    @Column(name = "tcacreatedat")
    private LocalDateTime tcacreatedat;

    @Column(name = "tcalastupdatedby")
    private Long tcalastupdatedby;

    @Column(name = "tcalastupdatedat")
    private LocalDateTime tcalastupdatedat;

    @Column(name = "tcarowstate")
    private Long tcarowstate;


    public Long getTcaid() { return tcaid; }
    public void setTcaid(Long tcaid) { this.tcaid = tcaid; }

    public Long getTcatcrid() { return tcatcrid; }
    public void setTcatcrid(Long tcatcrid) { this.tcatcrid = tcatcrid; }

    public Long getTcaprgid() { return tcaprgid; }
    public void setTcaprgid(Long tcaprgid) { this.tcaprgid = tcaprgid; }

    public String getTcastatus() { return tcastatus; }
    public void setTcastatus(String tcastatus) { this.tcastatus = tcastatus; }

    public Long getTcacreatedby() { return tcacreatedby; }
    public void setTcacreatedby(Long tcacreatedby) { this.tcacreatedby = tcacreatedby; }

    public LocalDateTime getTcacreatedat() { return tcacreatedat; }
    public void setTcacreatedat(LocalDateTime tcacreatedat) { this.tcacreatedat = tcacreatedat; }

    public Long getTcalastupdatedby() { return tcalastupdatedby; }
    public void setTcalastupdatedby(Long tcalastupdatedby) { this.tcalastupdatedby = tcalastupdatedby; }

    public LocalDateTime getTcalastupdatedat() { return tcalastupdatedat; }
    public void setTcalastupdatedat(LocalDateTime tcalastupdatedat) { this.tcalastupdatedat = tcalastupdatedat; }

    public Long getTcarowstate() { return tcarowstate; }
    public void setTcarowstate(Long tcarowstate) { this.tcarowstate = tcarowstate; }

}
