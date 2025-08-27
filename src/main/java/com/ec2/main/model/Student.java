package com.ec2.main.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "students",schema="ec2")
public class Student {

    @Id
    @Column(name = "stdid")
    private Long stdid;
    
    @Column(name = "stdinstid", nullable = false)
    private String stdinstid;
    
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

    private String stdfield1;
    private String stdfield2;
    private String stdfield3;

    public String getStdfield1() {
        return stdfield1;
    }

    public void setStdfield1(String stdfield1) {
        this.stdfield1 = stdfield1;
    }

    public String getStdfield2() {
        return stdfield2;
    }

    public void setStdfield2(String stdfield2) {
        this.stdfield2 = stdfield2;
    }

    public String getStdfield3() {
        return stdfield3;
    }

    public void setStdfield3(String stdfield3) {
        this.stdfield3 = stdfield3;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stdid", referencedColumnName = "stdid", insertable = false, updatable = false)
    private StudentProfile studentProfile;

    public StudentProfile getStudentProfile() {
        return studentProfile;
    }

    public void setStudentProfile(StudentProfile studentProfile) {
        this.studentProfile = studentProfile;
    }

    // Getters and Setters
    public Long getStdid() {
        return stdid;
    }

    public void setStdid(Long stdid) {
        this.stdid = stdid;
    }
    public String getStdinstid() {
        return stdinstid;
    }

    public void setStdinstid(String stdinstid) {
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
