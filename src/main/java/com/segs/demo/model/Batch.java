package com.segs.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "BATCHES")
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bchid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bchprgid")
    private Program program;

    public Long getBchid() {
        return bchid;
    }

    public void setBchid(Long bchid) {
        this.bchid = bchid;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    

}