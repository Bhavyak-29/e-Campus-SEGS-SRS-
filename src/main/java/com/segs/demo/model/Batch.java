package com.segs.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "batches",schema="ec2")
public class Batch {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
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