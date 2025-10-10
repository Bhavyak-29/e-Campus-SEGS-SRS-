package com.ec2.main.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="eggrademodification",schema="ec2")
public class eggrademodification {
    @Id
    @Column(name= "gmdid")
    private Long gmdid;

    @Column(name= "gmdtcrid")
    private Long gmdtcrid;

    @Column(name= "gmdexamtype_id")
    private Long gmdexamtype_id;

    @Column(name= "gmdstdid")
    private Long gmdstdid;

    @Column(name= "gmditerationno")
    private Long gmditerationno;

    @Column(name= "gmdpresentgrade")
    private Long gmdpresentgrade;

    @Column(name= "gmdnewgrade")
    private Long gmdnewgrade;

    @Column(name= "gmdchangedesc")
    private String gmdchangedesc;

    @Column(name= "gmdcreateby")
    private String gmdcreateby;

    @Column(name= "gmdcreatedt")
    private LocalDateTime gmdtcreatedt;

    @Column(name= "gmdupdateby")
    private String gmdupdateby;

    @Column(name= "gmdupdatedt")
    private LocalDateTime gmdtupdatedt;

    @Column(name= "gmdrowstate")
    private Long gmdrowstate;

    public Long getGmdid() {
        return gmdid;
    }

    public void setGmdid(Long gmdid) {
        this.gmdid = gmdid;
    }

    public Long getGmdtcrid() {
        return gmdtcrid;
    }

    public void setGmdtcrid(Long gmdtcrid) {
        this.gmdtcrid = gmdtcrid;
    }

    public Long getGmdexamtype_id() {
        return gmdexamtype_id;
    }

    public void setGmdexamtype_id(Long gmdexamtype_id) {
        this.gmdexamtype_id = gmdexamtype_id;
    }

    public Long getGmdstdid() {
        return gmdstdid;
    }

    public void setGmdstdid(Long gmdstdid) {
        this.gmdstdid = gmdstdid;
    }

    public Long getGmditerationno() {
        return gmditerationno;
    }

    public void setGmditerationno(Long gmditerationno) {
        this.gmditerationno = gmditerationno;
    }

    public Long getGmdpresentgrade() {
        return gmdpresentgrade;
    }

    public void setGmdpresentgrade(Long gmdpresentgrade) {
        this.gmdpresentgrade = gmdpresentgrade;
    }

    public Long getGmdnewgrade() {
        return gmdnewgrade;
    }

    public void setGmdnewgrade(Long gmdnewgrade) {
        this.gmdnewgrade = gmdnewgrade;
    }

    public String getGmdchangedesc() {
        return gmdchangedesc;
    }

    public void setGmdchangedesc(String gmdchangedesc) {
        this.gmdchangedesc = gmdchangedesc;
    }

    public String getGmdcreateby() {
        return gmdcreateby;
    }

    public void setGmdcreateby(String gmdcreateby) {
        this.gmdcreateby = gmdcreateby;
    }

    public LocalDateTime getGmdtcreatedt() {
        return gmdtcreatedt;
    }

    public void setGmdtcreatedt(LocalDateTime gmdtcreatedt) {
        this.gmdtcreatedt = gmdtcreatedt;
    }

    public String getGmdupdateby() {
        return gmdupdateby;
    }

    public void setGmdupdateby(String gmdupdateby) {
        this.gmdupdateby = gmdupdateby;
    }

    public LocalDateTime getGmdtupdatedt() {
        return gmdtupdatedt;
    }

    public void setGmdtupdatedt(LocalDateTime gmdtupdatedt) {
        this.gmdtupdatedt = gmdtupdatedt;
    }

    public Long getGmdrowstate() {
        return gmdrowstate;
    }

    public void setGmdrowstate(Long gmdrowstate) {
        this.gmdrowstate = gmdrowstate;
    }
}
