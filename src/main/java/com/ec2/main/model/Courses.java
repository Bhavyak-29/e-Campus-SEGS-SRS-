package com.ec2.main.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "courses", schema="ec2")
public class Courses {

    @Id
    @Column(name = "crsid")
    private Long crsid;

    @Column(name = "crscgpid")
    private Long crscgpid;

    @Column(name = "crsname")
    private String crsname;

    @Column(name = "crstitle")
    private String crstitle;

    @Column(name = "crscode")
    private String crscode;

    @Column(name = "crslongdesc")
    private String crslongdesc;

    @Column(name = "crsdiscipline")
    private String crsdiscipline;

    @Column(name = "crsassessmenttype")
    private String crsassessmenttype;

    @Column(name = "crslectures")
    private BigDecimal crslectures;

    @Column(name = "crstutorials")
    private BigDecimal crstutorials;

    @Column(name = "crspracticals")
    private BigDecimal crspracticals;

    @Column(name = "crscreditpoints")
    private BigDecimal crscreditpoints;

    @Column(name = "crsmarks")
    private Long crsmarks;

    @Column(name = "crsurl")
    private String crsurl;

    @Column(name = "crscorequisites")
    private String crscorequisites;

    @Column(name = "crsprerequisites")
    private String crsprerequisites;

    @Column(name = "crsequivalents")
    private String crsequivalents;

    @Column(name = "crs_short_name")
    private String crsShortName;

    @Column(name = "crs_stream")
    private String crsStream;

    @Column(name = "crs_cat")
    private String crsCat;

    @Column(name = "crscreatedby")
    private Long crscreatedby;

    @Column(name = "crscreatedat")
    private LocalDateTime crscreatedat;

    @Column(name = "crslastupdatedby")
    private Long crslastupdatedby;

    @Column(name = "crslastupdatedat")
    private LocalDateTime crslastupdatedat;

    @Column(name = "crsrowstate")
    private Long crsrowstate;

    @Column(name = "trmid", insertable = false, updatable = false)
    private Long trmid;

    @ManyToOne
    @JoinColumn(name = "trmid",referencedColumnName="trmid")
    private Terms term;

    public Terms getTerm() {
        return term;
    }

    public void setTerm(Terms term) {
        this.term = term;
    }

    public Long getTrmid() {
        return trmid;
    }

    public void setTrmid(Long trmid) {
        this.trmid = trmid;
    }


    public Long getCrsid() { return crsid; }
    public void setCrsid(Long crsid) { this.crsid = crsid; }

    public Long getCrscgpid() { return crscgpid; }
    public void setCrscgpid(Long crscgpid) { this.crscgpid = crscgpid; }

    public String getCrsname() { return crsname; }
    public void setCrsname(String crsname) { this.crsname = crsname; }

    public String getCrstitle() { return crstitle; }
    public void setCrstitle(String crstitle) { this.crstitle = crstitle; }

    public String getCrscode() { return crscode; }
    public void setCrscode(String crscode) { this.crscode = crscode; }

    public String getCrslongdesc() { return crslongdesc; }
    public void setCrslongdesc(String crslongdesc) { this.crslongdesc = crslongdesc; }

    public String getCrsdiscipline() { return crsdiscipline; }
    public void setCrsdiscipline(String crsdiscipline) { this.crsdiscipline = crsdiscipline; }

    public String getCrsassessmenttype() { return crsassessmenttype; }
    public void setCrsassessmenttype(String crsassessmenttype) { this.crsassessmenttype = crsassessmenttype; }

    public BigDecimal getCrslectures() { return crslectures; }
    public void setCrslectures(BigDecimal crslectures) { this.crslectures = crslectures; }

    public BigDecimal getCrstutorials() { return crstutorials; }
    public void setCrstutorials(BigDecimal crstutorials) { this.crstutorials = crstutorials; }

    public BigDecimal getCrspracticals() { return crspracticals; }
    public void setCrspracticals(BigDecimal crspracticals) { this.crspracticals = crspracticals; }

    public BigDecimal getCrscreditpoints() { return crscreditpoints; }
    public void setCrscreditpoints(BigDecimal crscreditpoints) { this.crscreditpoints = crscreditpoints; }

    public Long getCrsmarks() { return crsmarks; }
    public void setCrsmarks(Long crsmarks) { this.crsmarks = crsmarks; }

    public String getCrsurl() { return crsurl; }
    public void setCrsurl(String crsurl) { this.crsurl = crsurl; }

    public String getCrscorequisites() { return crscorequisites; }
    public void setCrscorequisites(String crscorequisites) { this.crscorequisites = crscorequisites; }

    public String getCrsprerequisites() { return crsprerequisites; }
    public void setCrsprerequisites(String crsprerequisites) { this.crsprerequisites = crsprerequisites; }

    public String getCrsequivalents() { return crsequivalents; }
    public void setCrsequivalents(String crsequivalents) { this.crsequivalents = crsequivalents; }

    public String getCrsshortname() { return crsShortName; }
    public void setCrsshortname(String crsShortName) { this.crsShortName = crsShortName; }

    public String getCrsstream() { return crsStream; }
    public void setCrsstream(String crsStream) { this.crsStream = crsStream; }

    public String getCrscat() { return crsCat; }
    public void setCrscat(String crsCat) { this.crsCat = crsCat; }

    public Long getCrscreatedby() { return crscreatedby; }
    public void setCrscreatedby(Long crscreatedby) { this.crscreatedby = crscreatedby; }

    public LocalDateTime getCrscreatedat() { return crscreatedat; }
    public void setCrscreatedat(LocalDateTime crscreatedat) { this.crscreatedat = crscreatedat; }

    public Long getCrslastupdatedby() { return crslastupdatedby; }
    public void setCrslastupdatedby(Long crslastupdatedby) { this.crslastupdatedby = crslastupdatedby; }

    public LocalDateTime getCrslastupdatedat() { return crslastupdatedat; }
    public void setCrslastupdatedat(LocalDateTime crslastupdatedat) { this.crslastupdatedat = crslastupdatedat; }

    public Long getCrsrowstate() { return crsrowstate; }
    public void setCrsrowstate(Long crsrowstate) { this.crsrowstate = crsrowstate; }

}
