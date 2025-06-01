package com.segs.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "STUDENTS")
public class Student {

    @Id
    @Column(name = "STUDENTID")
    private Long stdinstid;

    private String stdfirstname;
    private String stdlastname;
    private int stdrowstate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stdbchid")
    private Batch batch;

    @Column(name = "CURRADRID")
    private Long currAdrId;

    @Column(name = "PRMTADRID")
    private Long prmtAdrId;

    @Column(name = "EMGRADRID")
    private Long emrgAdrId;

    private String stdemail;

    // Getters and Setters

    public Long getStdinstid() {
        return stdinstid;
    }

    public void setStdinstid(Long stdinstid) {
        this.stdinstid = stdinstid;
    }

    public String getStdfirstname() {
        return stdfirstname;
    }

    public void setStdfirstname(String stdfirstname) {
        this.stdfirstname = stdfirstname;
    }

    public String getStdlastname() {
        return stdlastname;
    }

    public void setStdlastname(String stdlastname) {
        this.stdlastname = stdlastname;
    }

    public int getStdrowstate() {
        return stdrowstate;
    }

    public void setStdrowstate(int stdrowstate) {
        this.stdrowstate = stdrowstate;
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public Long getCurrAdrId() {
        return currAdrId;
    }

    public void setCurrAdrId(Long currAdrId) {
        this.currAdrId = currAdrId;
    }

    public Long getPrmtAdrId() {
        return prmtAdrId;
    }

    public void setPrmtAdrId(Long prmtAdrId) {
        this.prmtAdrId = prmtAdrId;
    }

    public Long getEmrgAdrId() {
        return emrgAdrId;
    }

    public void setEmrgAdrId(Long emrgAdrId) {
        this.emrgAdrId = emrgAdrId;
    }

    public String getStdemail() {
        return stdemail;
    }

    public void setStdemail(String stdemail) {
        this.stdemail = stdemail;
    }
}
