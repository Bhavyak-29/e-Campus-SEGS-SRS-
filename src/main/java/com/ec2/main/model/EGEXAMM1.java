package com.ec2.main.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "egexamm1", schema="ec2")
public class EGEXAMM1 {

    @Id
    @Column(name = "examtype_id")
    private Long examtypeId;

    @Column(name = "examtype_title")
    private String examtypeTitle;

    @Column(name = "examtype_description")
    private String examtypeDescription;

    @Column(name = "creat_by")
    private Long creatBy;

    @Column(name = "creat_dt")
    private LocalDateTime creatDt;

    @Column(name = "updat_by")
    private Long updatBy;

    @Column(name = "updat_dt")
    private LocalDateTime updatDt;

    @Column(name = "row_st")
    private Long rowSt;


    public Long getExamtypeid() { return examtypeId; }
    public void setExamtypeid(Long examtypeId) { this.examtypeId = examtypeId; }

    public String getExamtypetitle() { return examtypeTitle; }
    public void setExamtypetitle(String examtypeTitle) { this.examtypeTitle = examtypeTitle; }

    public String getExamtypedescription() { return examtypeDescription; }
    public void setExamtypedescription(String examtypeDescription) { this.examtypeDescription = examtypeDescription; }

    public Long getCreatby() { return creatBy; }
    public void setCreatby(Long creatBy) { this.creatBy = creatBy; }

    public LocalDateTime getCreatdt() { return creatDt; }
    public void setCreatdt(LocalDateTime creatDt) { this.creatDt = creatDt; }

    public Long getUpdatby() { return updatBy; }
    public void setUpdatby(Long updatBy) { this.updatBy = updatBy; }

    public LocalDateTime getUpdatdt() { return updatDt; }
    public void setUpdatdt(LocalDateTime updatDt) { this.updatDt = updatDt; }

    public Long getRowst() { return rowSt; }
    public void setRowst(Long rowSt) { this.rowSt = rowSt; }

}
