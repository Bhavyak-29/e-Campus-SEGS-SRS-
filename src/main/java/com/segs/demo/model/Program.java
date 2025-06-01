package com.segs.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "PROGRAMS")
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
