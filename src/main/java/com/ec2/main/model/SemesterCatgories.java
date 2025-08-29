package com.ec2.main.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "semestercategories", schema="ec2")
public class SemesterCatgories {

    @Id
    @Column(name = "stcid")
    private Long stcid;

    @Column(name = "stcname")
    private String stcname;

    @Column(name = "stcdescription")
    private String stcdescription;

    @Column(name = "stcshortname")
    private String stcshortname;

    @Column(name = "stcfield5")
    private LocalDateTime stcfield5;

    @Column(name = "stccreatedby")
    private Long stccreatedby;

    @Column(name = "stccreatedat")
    private LocalDateTime stccreatedat;

    @Column(name = "stclastupdatedby")
    private Long stclastupdatedby;

    @Column(name = "stclastupdatedat")
    private LocalDateTime stclastupdatedat;

    @Column(name = "stcrowstate")
    private Long stcrowstate;


    public Long getStcid() { return stcid; }
    public void setStcid(Long stcid) { this.stcid = stcid; }

    public String getStcname() { return stcname; }
    public void setStcname(String stcname) { this.stcname = stcname; }

    public String getStcdescription() { return stcdescription; }
    public void setStcdescription(String stcdescription) { this.stcdescription = stcdescription; }

    public String getStcshortname() { return stcshortname; }
    public void setStcshortname(String stcshortname) { this.stcshortname = stcshortname; }

    public LocalDateTime getStcfield5() { return stcfield5; }
    public void setStcfield5(LocalDateTime stcfield5) { this.stcfield5 = stcfield5; }

    public Long getStccreatedby() { return stccreatedby; }
    public void setStccreatedby(Long stccreatedby) { this.stccreatedby = stccreatedby; }

    public LocalDateTime getStccreatedat() { return stccreatedat; }
    public void setStccreatedat(LocalDateTime stccreatedat) { this.stccreatedat = stccreatedat; }

    public Long getStclastupdatedby() { return stclastupdatedby; }
    public void setStclastupdatedby(Long stclastupdatedby) { this.stclastupdatedby = stclastupdatedby; }

    public LocalDateTime getStclastupdatedat() { return stclastupdatedat; }
    public void setStclastupdatedat(LocalDateTime stclastupdatedat) { this.stclastupdatedat = stclastupdatedat; }

    public Long getStcrowstate() { return stcrowstate; }
    public void setStcrowstate(Long stcrowstate) { this.stcrowstate = stcrowstate; }

}
