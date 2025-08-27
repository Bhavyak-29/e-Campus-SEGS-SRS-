package com.ec2.main.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "egcrstt1", schema = "ec2")  // use schema name if required
public class Egcrstt1 {

    @EmbeddedId
    private Egcrstt1Id id;

    @Column(name = "obt_mks")
    private Float obtainedMarks;

    @Column(name = "obtgr_id")
    private Integer obtainedGradeId;

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

    // Getters and Setters

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

    public Integer getObtainedGradeId() {
        return obtainedGradeId;
    }

    public void setObtainedGradeId(Integer obtainedGradeId) {
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



























































// package com.ec2.main.model;
// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.Table;

// @Entity
// @Table(name = "egexamm1", schema = "ec2")
// public class ExamType {
//     @Id
//     @Column(name = "examtype_id")
//     private Long id;

//     @Column(name = "examtype_title")
//     private String title;

//     @Column(name = "row_st")
//     private Integer rowState;

//     @ManyToOne
//     @JoinColumn(name = "CRSID") // ← replace with actual column name in table
//     private Course course;

//     public Long getId() {
//         return id;
//     }

//     public void setId(Long id) {
//         this.id = id;
//     }

//     public String getTitle() {
//         return title;
//     }

//     public void setTitle(String title) {
//         this.title = title;
//     }

//     public Integer getRowState() {
//         return rowState;
//     }

//     public void setRowState(Integer rowState) {
//         this.rowState = rowState;
//     }

//     public Course getCourse() {
//         return course;
//     }

//     public void setCourse(Course course) {
//         this.course = course;
//     }
// }
