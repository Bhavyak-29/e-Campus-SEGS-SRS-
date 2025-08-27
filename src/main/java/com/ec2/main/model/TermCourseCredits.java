package com.ec2.main.model;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table; 

@Entity
@Table(name = "termcoursecredits", schema = "ec2") // Assuming a schema 'ec2' and table 'term_course_credits'
public class TermCourseCredits {

    @Id
    @Column(name = "tccid") // Primary Key
    private Long tccid;

    @Column(name = "tcctcrid")
    private Long tcctcrid;

    @Column(name = "tcccreditpoints", precision = 38, scale = 2) // NUMERIC(38,2)
    private BigDecimal tcccreditpoints;

    public TermCourseCredits() {
        // Default constructor for JPA
    }

    // Getters and Setters
    public Long getTccid() {
        return tccid;
    }

    public void setTccid(Long tccid) {
        this.tccid = tccid;
    }

    public Long getTcctcrid() {
        return tcctcrid;
    }

    public void setTcctcrid(Long tcctcrid) {
        this.tcctcrid = tcctcrid;
    }

    public BigDecimal getTcccreditpoints() {
        return tcccreditpoints;
    }

    public void setTcccreditpoints(BigDecimal tcccreditpoints) {
        this.tcccreditpoints = tcccreditpoints;
    }
}