package com.segs.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "studentregistration",schema="ec2")
public class StudentRegistration {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long srgid;
    private Long srgstdid;
    private Long srgstrid;
    private int srgrowstate;
    
    public Long getSrgid() {
        return srgid;
    }
    public void setSrgid(Long srgid) {
        this.srgid = srgid;
    }
    public Long getSrgstdid() {
        return srgstdid;
    }
    public void setSrgstdid(Long srgstdid) {
        this.srgstdid = srgstdid;
    }
    public Long getSrgstrid() {
        return srgstrid;
    }
    public void setSrgstrid(Long srgstrid) {
        this.srgstrid = srgstrid;
    }
    public int getSrgrowstate() {
        return srgrowstate;
    }
    public void setSrgrowstate(int srgrowstate) {
        this.srgrowstate = srgrowstate;
    }

    
}

