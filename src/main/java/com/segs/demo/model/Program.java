package com.segs.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "programs",schema="ec2")
public class Program {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prgid;

    private String prgname;

    public Long getPrgid() {
        return prgid;
    }

    public void setPrgid(Long prgid) {
        this.prgid = prgid;
    }

    public String getPrgname() {
        return prgname;
    }

    public void setPrgname(String prgname) {
        this.prgname = prgname;
    }

    
}
