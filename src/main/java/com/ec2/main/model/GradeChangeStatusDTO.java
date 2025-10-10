package com.ec2.main.model;

public class GradeChangeStatusDTO {
    private Long gmdid;
    public Long getGmdid() {
        return gmdid;
    }

    public void setGmdid(Long gmdid) {
        this.gmdid = gmdid;
    }
    private String studentInstituteId;   // instead of Long studId
    private String presentGradeLetter;   // instead of Long gradeId
    private String newGradeLetter;       // instead of Long gradeId
    public String getStudentInstituteId() {
        return studentInstituteId;
    }
    public void setStudentInstituteId(String studentInstituteId) {
        this.studentInstituteId = studentInstituteId;
    }
    public String getPresentGradeLetter() {
        return presentGradeLetter;
    }
    public void setPresentGradeLetter(String presentGradeLetter) {
        this.presentGradeLetter = presentGradeLetter;
    }
    public String getNewGradeLetter() {
        return newGradeLetter;
    }
    public void setNewGradeLetter(String newGradeLetter) {
        this.newGradeLetter = newGradeLetter;
    }
    public String getStatusSubmitted() {
        return statusSubmitted;
    }
    public void setStatusSubmitted(String statusSubmitted) {
        this.statusSubmitted = statusSubmitted;
    }
    public String getStatusDean() {
        return statusDean;
    }
    public void setStatusDean(String statusDean) {
        this.statusDean = statusDean;
    }
    public String getStatusRegistrar() {
        return statusRegistrar;
    }
    public void setStatusRegistrar(String statusRegistrar) {
        this.statusRegistrar = statusRegistrar;
    }
    private String statusSubmitted;
    private String statusDean;
    private String statusRegistrar;
    private String remarks;
    public GradeChangeStatusDTO() {}
    public GradeChangeStatusDTO(
        String studentInstituteId,
        String presentGradeLetter,
        String newGradeLetter,
        Long gmdid,
        String statusSubmitted,
        String statusDean,
        String statusRegistrar,
        String remarks
    ) {
        this.studentInstituteId = studentInstituteId;
        this.presentGradeLetter = presentGradeLetter;
        this.newGradeLetter = newGradeLetter;
        this.gmdid = gmdid;
        this.statusSubmitted = statusSubmitted;
        this.statusDean = statusDean;
        this.statusRegistrar = statusRegistrar;
        this.remarks = remarks;
    }

    // getters + setters

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}