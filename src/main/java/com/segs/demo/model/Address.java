package com.segs.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ADDRESSES")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adrid;

    private String adrline1;
    private String adrline2;
    public Long getAdrid() {
        return adrid;
    }
    public void setAdrid(Long adrid) {
        this.adrid = adrid;
    }
    public String getAdrline1() {
        return adrline1;
    }
    public void setAdrline1(String adrline1) {
        this.adrline1 = adrline1;
    }
    public String getAdrline2() {
        return adrline2;
    }
    public void setAdrline2(String adrline2) {
        this.adrline2 = adrline2;
    }

}
