package com.ec2.main.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "coursegroups", schema="ec2")
public class CourseGroups {

    @Id
    @Column(name = "cgpid")
    private Long cgpid;

    @Column(name = "cgpname")
    private String cgpname;

    @Column(name = "cgpcgtid")
    private Long cgpcgtid;

    @Column(name = "cgptrmid")
    private Long cgptrmid;

    @Column(name = "cgpcreatedby")
    private Long cgpcreatedby;

    @Column(name = "cgpcreatedat")
    private LocalDateTime cgpcreatedat;

    @Column(name = "cgplastupdatedby")
    private Long cgplastupdatedby;

    @Column(name = "cgplastupdatedat")
    private LocalDateTime cgplastupdatedat;

    @Column(name = "cgprowstate")
    private Long cgprowstate;


    public Long getCgpid() { return cgpid; }
    public void setCgpid(Long cgpid) { this.cgpid = cgpid; }

    public String getCgpname() { return cgpname; }
    public void setCgpname(String cgpname) { this.cgpname = cgpname; }

    public Long getCgpcgtid() { return cgpcgtid; }
    public void setCgpcgtid(Long cgpcgtid) { this.cgpcgtid = cgpcgtid; }

    public Long getCgptrmid() { return cgptrmid; }
    public void setCgptrmid(Long cgptrmid) { this.cgptrmid = cgptrmid; }

    public Long getCgpcreatedby() { return cgpcreatedby; }
    public void setCgpcreatedby(Long cgpcreatedby) { this.cgpcreatedby = cgpcreatedby; }

    public LocalDateTime getCgpcreatedat() { return cgpcreatedat; }
    public void setCgpcreatedat(LocalDateTime cgpcreatedat) { this.cgpcreatedat = cgpcreatedat; }

    public Long getCgplastupdatedby() { return cgplastupdatedby; }
    public void setCgplastupdatedby(Long cgplastupdatedby) { this.cgplastupdatedby = cgplastupdatedby; }

    public LocalDateTime getCgplastupdatedat() { return cgplastupdatedat; }
    public void setCgplastupdatedat(LocalDateTime cgplastupdatedat) { this.cgplastupdatedat = cgplastupdatedat; }

    public Long getCgprowstate() { return cgprowstate; }
    public void setCgprowstate(Long cgprowstate) { this.cgprowstate = cgprowstate; }

}
