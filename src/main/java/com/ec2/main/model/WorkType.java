package com.ec2.main.model;

import jakarta.persistence.*;

@Entity
@Table(name = "work_types", schema = "ec2")
public class WorkType {

    @Id
    @Column(name = "code", nullable = false)
    private Short code;

    @Column(name = "description", length = 30)
    private String description;

    @Column(name = "display", length = 5)
    private String display;

    @Column(name = "jspfilepath", length = 100)
    private String jspFilePath;

    // Getters and setters (if you want, or use Lombok)

    public Short getCode() {
        return code;
    }

    public void setCode(Short code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getJspFilePath() {
        return jspFilePath;
    }

    public void setJspFilePath(String jspFilePath) {
        this.jspFilePath = jspFilePath;
    }
}

