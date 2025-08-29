package com.ec2.main.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "coursegroupcourses", schema="ec2")
public class CourseGroupCourses {

    @Id
    @Column(name = "cgcid")
    private Long cgcid;

    @Column(name = "cgccgpid")
    private Long cgccgpid;

    @Column(name = "cgccrsid")
    private Long cgccrsid;

    @Column(name = "cgcfield1")
    private Long cgcfield1;

    @Column(name = "cgccreatedby")
    private Long cgccreatedby;

    @Column(name = "cgccreatedat")
    private LocalDateTime cgccreatedat;

    @Column(name = "cgclastupdatedby")
    private Long cgclastupdatedby;

    @Column(name = "cgclastupdatedat")
    private LocalDateTime cgclastupdatedat;

    @Column(name = "cgcrowstate")
    private Long cgcrowstate;


    public Long getCgcid() { return cgcid; }
    public void setCgcid(Long cgcid) { this.cgcid = cgcid; }

    public Long getCgccgpid() { return cgccgpid; }
    public void setCgccgpid(Long cgccgpid) { this.cgccgpid = cgccgpid; }

    public Long getCgccrsid() { return cgccrsid; }
    public void setCgccrsid(Long cgccrsid) { this.cgccrsid = cgccrsid; }

    public Long getCgcfield1() { return cgcfield1; }
    public void setCgcfield1(Long cgcfield1) { this.cgcfield1 = cgcfield1; }

    public Long getCgccreatedby() { return cgccreatedby; }
    public void setCgccreatedby(Long cgccreatedby) { this.cgccreatedby = cgccreatedby; }

    public LocalDateTime getCgccreatedat() { return cgccreatedat; }
    public void setCgccreatedat(LocalDateTime cgccreatedat) { this.cgccreatedat = cgccreatedat; }

    public Long getCgclastupdatedby() { return cgclastupdatedby; }
    public void setCgclastupdatedby(Long cgclastupdatedby) { this.cgclastupdatedby = cgclastupdatedby; }

    public LocalDateTime getCgclastupdatedat() { return cgclastupdatedat; }
    public void setCgclastupdatedat(LocalDateTime cgclastupdatedat) { this.cgclastupdatedat = cgclastupdatedat; }

    public Long getCgcrowstate() { return cgcrowstate; }
    public void setCgcrowstate(Long cgcrowstate) { this.cgcrowstate = cgcrowstate; }

}
