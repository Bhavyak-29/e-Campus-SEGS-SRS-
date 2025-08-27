package com.ec2.main.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "courses", schema = "ec2")
public class Course {

    @Id
    @Column(name = "crsid")
    private Long id;

    @Column(name = "crsname")
    private String name;

    @Column(name = "crscode")
    private String code;

    @Column(name = "crsrowstate")
    private Integer rowState;

    @ManyToOne
    @JoinColumn(name = "trmid")
    private Term term;

    // --- Additional Fields ---

    @Column(name = "crscgpid")
    private Short cgpid;

    @Column(name = "crstitle")
    private String title;

    @Column(name = "crslongdesc")
    private String longDescription;

    @Column(name = "crsdiscipline")
    private String discipline;

    @Column(name = "crsassessmenttype")
    private String assessmentType;

    @Column(name = "crslectures")
    private BigDecimal lectures;

    @Column(name = "crstutorials")
    private BigDecimal tutorials;

    @Column(name = "crspracticals")
    private BigDecimal practicals;

    @Column(name = "crscreditpoints")
    private BigDecimal creditPoints;
 
    @Column(name = "crsmarks")
    private Short marks;

    @Column(name = "crsurl")
    private String url;

    @Column(name = "crscorequisites")
    private String corequisites;

    @Column(name = "crsprerequisites")
    private String prerequisites;

    @Column(name = "crsequivalents")
    private String equivalents;

    @Column(name = "crs_short_name")
    private String shortName;

    @Column(name = "crs_stream")
    private String stream;

    @Column(name = "crs_cat")
    private String category;

    @Column(name = "crscreatedby")
    private Long createdBy;

    @Column(name = "crscreatedat")
    private Timestamp createdAt;

    @Column(name = "crslastupdatedby")
    private Long lastUpdatedBy;

    @Column(name = "crslastupdatedat")
    private Timestamp lastUpdatedAt;

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getRowState() {
        return rowState;
    }

    public void setRowState(Integer rowState) {
        this.rowState = rowState;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public Short getCgpid() {
        return cgpid;
    }

    public void setCgpid(Short cgpid) {
        this.cgpid = cgpid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public BigDecimal getLectures() {
        return lectures;
    }

    public void setLectures(BigDecimal lectures) {
        this.lectures = lectures;
    }

    public BigDecimal getTutorials() {
        return tutorials;
    }

    public void setTutorials(BigDecimal tutorials) {
        this.tutorials = tutorials;
    }

    public BigDecimal getPracticals() {
        return practicals;
    }

    public void setPracticals(BigDecimal practicals) {
        this.practicals = practicals;
    }

    public BigDecimal getCreditPoints() {
        return creditPoints;
    }

    public void setCreditPoints(BigDecimal creditPoints) {
        this.creditPoints = creditPoints;
    }

    public Short getMarks() {
        return marks;
    }

    public void setMarks(Short marks) {
        this.marks = marks;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCorequisites() {
        return corequisites;
    }

    public void setCorequisites(String corequisites) {
        this.corequisites = corequisites;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getEquivalents() {
        return equivalents;
    }

    public void setEquivalents(String equivalents) {
        this.equivalents = equivalents;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Timestamp getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Timestamp lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }
}
