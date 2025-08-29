package com.ec2.main.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "addresses", schema="ec2")
public class Addresses {

    @Id
    @Column(name = "adrid")
    private Integer adrid;

    @Column(name = "adrcontact")
    private String adrcontact;

    @Column(name = "address")
    private String address;

    @Column(name = "adrcity")
    private String adrcity;

    @Column(name = "adrdistrict")
    private String adrdistrict;

    @Column(name = "adrstate")
    private String adrstate;

    @Column(name = "adrcountry")
    private String adrcountry;

    @Column(name = "adrpincode")
    private String adrpincode;

    @Column(name = "adrtelno")
    private String adrtelno;

    @Column(name = "adrfaxno")
    private String adrfaxno;

    @Column(name = "adremailid")
    private String adremailid;

    @Column(name = "adrcreatedby")
    private Long adrcreatedby;

    @Column(name = "adrcreatedat")
    private LocalDateTime adrcreatedat;

    @Column(name = "adrlastupdatedby")
    private Long adrlastupdatedby;

    @Column(name = "adrlastupdatedat")
    private LocalDateTime adrlastupdatedat;

    @Column(name = "adrrowstate")
    private Long adrrowstate;


    public Integer getAdrid() { return adrid; }
    public void setAdrid(Integer adrid) { this.adrid = adrid; }

    public String getAdrcontact() { return adrcontact; }
    public void setAdrcontact(String adrcontact) { this.adrcontact = adrcontact; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getAdrcity() { return adrcity; }
    public void setAdrcity(String adrcity) { this.adrcity = adrcity; }

    public String getAdrdistrict() { return adrdistrict; }
    public void setAdrdistrict(String adrdistrict) { this.adrdistrict = adrdistrict; }

    public String getAdrstate() { return adrstate; }
    public void setAdrstate(String adrstate) { this.adrstate = adrstate; }

    public String getAdrcountry() { return adrcountry; }
    public void setAdrcountry(String adrcountry) { this.adrcountry = adrcountry; }

    public String getAdrpincode() { return adrpincode; }
    public void setAdrpincode(String adrpincode) { this.adrpincode = adrpincode; }

    public String getAdrtelno() { return adrtelno; }
    public void setAdrtelno(String adrtelno) { this.adrtelno = adrtelno; }

    public String getAdrfaxno() { return adrfaxno; }
    public void setAdrfaxno(String adrfaxno) { this.adrfaxno = adrfaxno; }

    public String getAdremailid() { return adremailid; }
    public void setAdremailid(String adremailid) { this.adremailid = adremailid; }

    public Long getAdrcreatedby() { return adrcreatedby; }
    public void setAdrcreatedby(Long adrcreatedby) { this.adrcreatedby = adrcreatedby; }

    public LocalDateTime getAdrcreatedat() { return adrcreatedat; }
    public void setAdrcreatedat(LocalDateTime adrcreatedat) { this.adrcreatedat = adrcreatedat; }

    public Long getAdrlastupdatedby() { return adrlastupdatedby; }
    public void setAdrlastupdatedby(Long adrlastupdatedby) { this.adrlastupdatedby = adrlastupdatedby; }

    public LocalDateTime getAdrlastupdatedat() { return adrlastupdatedat; }
    public void setAdrlastupdatedat(LocalDateTime adrlastupdatedat) { this.adrlastupdatedat = adrlastupdatedat; }

    public Long getAdrrowstate() { return adrrowstate; }
    public void setAdrrowstate(Long adrrowstate) { this.adrrowstate = adrrowstate; }

}
