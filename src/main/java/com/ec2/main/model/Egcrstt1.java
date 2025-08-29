package com.ec2.main.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


//toupdate

@Entity
@Table(name = "egcrstt1", schema = "ec2")  // use schema name if required
public class Egcrstt1 {

    @EmbeddedId
    private Egcrstt1Id id;

    @Column(name = "obt_mks")
    private Float obtainedMarks;

    @Column(name = "obtgr_id")
    private Long obtainedGradeId;

    @Column(name = "obt_credits", precision = 9, scale = 2)
    private BigDecimal obtainedCredits;

    @Column(name = "crst_field1")
    private String customField1;

    @Column(name = "creat_by")
    private Long createdBy;

    @Column(name = "creat_dt")
    private LocalDateTime createdDate;

    @Column(name = "updat_by")
    private Long updatedBy;

    @Column(name = "updat_dt")
    private LocalDateTime updatedDate;

    @Column(name = "row_st")
    private String rowStatus;

    @Column(name = "crsid")
    private Long crsId;

    public Egcrstt1Id getId() {
        return id;
    }

    public void setId(Egcrstt1Id id) {
        this.id = id;
    }

    public Float getObtainedMarks() {
        return obtainedMarks;
    }

    public void setObtainedMarks(Float obtainedMarks) {
        this.obtainedMarks = obtainedMarks;
    }

    public Long getObtainedGradeId() {
        return obtainedGradeId;
    }

    public void setObtainedGradeId(Long obtainedGradeId) {
        this.obtainedGradeId = obtainedGradeId;
    }

    public BigDecimal getObtainedCredits() {
        return obtainedCredits;
    }

    public void setObtainedCredits(BigDecimal obtainedCredits) {
        this.obtainedCredits = obtainedCredits;
    }

    public String getCustomField1() {
        return customField1;
    }

    public void setCustomField1(String customField1) {
        this.customField1 = customField1;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getRowStatus() {
        return rowStatus;
    }

    public void setRowStatus(String rowStatus) {
        this.rowStatus = rowStatus;
    }

    public Long getCrsId() {
        return crsId;
    }

    public void setCrsId(Long crsId) {
        this.crsId = crsId;
    }
}