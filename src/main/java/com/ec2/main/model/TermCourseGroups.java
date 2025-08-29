package com.ec2.main.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "termcoursegroups", schema="ec2")
public class TermCourseGroups {

    @Id
    @Column(name = "tcgid")
    private Long tcgid;

    @Column(name = "tcgcgpid")
    private Long tcgcgpid;

    @Column(name = "tcgtrmid")
    private Long tcgtrmid;

    @Column(name = "tcgcreatedby")
    private Long tcgcreatedby;

    @Column(name = "tcgcreatedat")
    private LocalDateTime tcgcreatedat;

    @Column(name = "tcglastupdatedby")
    private Long tcglastupdatedby;

    @Column(name = "tcglastupdatedat")
    private LocalDateTime tcglastupdatedat;

    @Column(name = "tcgrowstate")
    private Long tcgrowstate;


    public Long getTcgid() { return tcgid; }
    public void setTcgid(Long tcgid) { this.tcgid = tcgid; }

    public Long getTcgcgpid() { return tcgcgpid; }
    public void setTcgcgpid(Long tcgcgpid) { this.tcgcgpid = tcgcgpid; }

    public Long getTcgtrmid() { return tcgtrmid; }
    public void setTcgtrmid(Long tcgtrmid) { this.tcgtrmid = tcgtrmid; }

    public Long getTcgcreatedby() { return tcgcreatedby; }
    public void setTcgcreatedby(Long tcgcreatedby) { this.tcgcreatedby = tcgcreatedby; }

    public LocalDateTime getTcgcreatedat() { return tcgcreatedat; }
    public void setTcgcreatedat(LocalDateTime tcgcreatedat) { this.tcgcreatedat = tcgcreatedat; }

    public Long getTcglastupdatedby() { return tcglastupdatedby; }
    public void setTcglastupdatedby(Long tcglastupdatedby) { this.tcglastupdatedby = tcglastupdatedby; }

    public LocalDateTime getTcglastupdatedat() { return tcglastupdatedat; }
    public void setTcglastupdatedat(LocalDateTime tcglastupdatedat) { this.tcglastupdatedat = tcglastupdatedat; }

    public Long getTcgrowstate() { return tcgrowstate; }
    public void setTcgrowstate(Long tcgrowstate) { this.tcgrowstate = tcgrowstate; }

}
