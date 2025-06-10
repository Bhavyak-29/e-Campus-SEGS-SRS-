package com.segs.demo.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class Egcrstt1Id implements Serializable {

    private Long tcrid;
    private Long examtypeId;
    private String studId;

    // Getters and setters

    public Long getTcrid() {
        return tcrid;
    }

    public void setTcrid(Long tcrid) {
        this.tcrid = tcrid;
    }

    public Long getExamtypeId() {
        return examtypeId;
    }

    public void setExamtypeId(Long examtypeId) {
        this.examtypeId = examtypeId;
    }

    public String getStudId() {
        return studId;
    }

    public void setStudId(String studId) {
        this.studId = studId;
    }

    // equals() and hashCode() — required for composite keys

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Egcrstt1Id that)) return false;
        return Objects.equals(tcrid, that.tcrid) &&
               Objects.equals(examtypeId, that.examtypeId) &&
               Objects.equals(studId, that.studId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tcrid, examtypeId, studId);
    }
}
