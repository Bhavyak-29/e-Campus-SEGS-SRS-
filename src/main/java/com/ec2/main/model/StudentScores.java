package com.ec2.main.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "studentscores", schema="ec2")
public class StudentScores {

    @Id
    @Column(name = "sscid")
    private Long sscid;

    @Column(name = "sscstdid")
    private Long sscstdid;

    @Column(name = "ssctcrid")
    private Integer ssctcrid;

    @Column(name = "sscgrade")
    private String sscgrade;

    @Column(name = "sscfield1")
    private String sscfield1;

    @Column(name = "sscfield2")
    private String sscfield2;

    @Column(name = "sscfield3")
    private String sscfield3;

    @Column(name = "ssccreatedby")
    private Long ssccreatedby;

    @Column(name = "ssccreatedat")
    private LocalDateTime ssccreatedat;

    @Column(name = "ssclastupdatedby")
    private Long ssclastupdatedby;

    @Column(name = "ssclastupdatedat")
    private LocalDateTime ssclastupdatedat;

    @Column(name = "sscrowstate")
    private Integer sscrowstate;


    public Long getSscid() { return sscid; }
    public void setSscid(Long sscid) { this.sscid = sscid; }

    public Long getSscstdid() { return sscstdid; }
    public void setSscstdid(Long sscstdid) { this.sscstdid = sscstdid; }

    public Integer getSsctcrid() { return ssctcrid; }
    public void setSsctcrid(Integer ssctcrid) { this.ssctcrid = ssctcrid; }

    public String getSscgrade() { return sscgrade; }
    public void setSscgrade(String sscgrade) { this.sscgrade = sscgrade; }

    public String getSscfield1() { return sscfield1; }
    public void setSscfield1(String sscfield1) { this.sscfield1 = sscfield1; }

    public String getSscfield2() { return sscfield2; }
    public void setSscfield2(String sscfield2) { this.sscfield2 = sscfield2; }

    public String getSscfield3() { return sscfield3; }
    public void setSscfield3(String sscfield3) { this.sscfield3 = sscfield3; }

    public Long getSsccreatedby() { return ssccreatedby; }
    public void setSsccreatedby(Long ssccreatedby) { this.ssccreatedby = ssccreatedby; }

    public LocalDateTime getSsccreatedat() { return ssccreatedat; }
    public void setSsccreatedat(LocalDateTime ssccreatedat) { this.ssccreatedat = ssccreatedat; }

    public Long getSsclastupdatedby() { return ssclastupdatedby; }
    public void setSsclastupdatedby(Long ssclastupdatedby) { this.ssclastupdatedby = ssclastupdatedby; }

    public LocalDateTime getSsclastupdatedat() { return ssclastupdatedat; }
    public void setSsclastupdatedat(LocalDateTime ssclastupdatedat) { this.ssclastupdatedat = ssclastupdatedat; }

    public Integer getSscrowstate() { return sscrowstate; }
    public void setSscrowstate(Integer sscrowstate) { this.sscrowstate = sscrowstate; }

}
