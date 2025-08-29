package com.ec2.main.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;



//toupdate

@Entity
@Table(name = "studentsemesterresult", schema = "ec2")
public class StudentSemesterResult {

    @Id
    @Column(name = "ssrid")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ssrsrgid", nullable = false)
    private StudentRegistrations studentRegistration;

    @ManyToOne
    @JoinColumn(name = "grade") 
    private StudentGrade grade;

    @Column(name = "ssrcreditsregistered", nullable = false, precision = 38, scale = 2)
    private BigDecimal creditsRegistered;

    @Column(name = "ssrcreditsearned", nullable = false, precision = 38, scale = 2)
    private BigDecimal creditsEarned;

    @Column(name = "ssrgradepointsearned", nullable = false, precision = 38, scale = 2)
    private BigDecimal gradePointsEarned;

    @Column(name = "ssrcumcreditsregistered", nullable = false, precision = 38, scale = 2)
    private BigDecimal cumCreditsRegistered;

    @Column(name = "ssrcumcreditsearned", nullable = false, precision = 38, scale = 2)
    private BigDecimal cumCreditsEarned;

    @Column(name = "ssrcumgradepointsearned", nullable = false, precision = 38, scale = 2)
    private BigDecimal cumGradePointsEarned;

    @Column(name = "ssrspigradepoint", nullable = false, precision = 38, scale = 2)
    private BigDecimal spiGradePoints;

    @Column(name = "ssrcpigradepoints", precision = 38, scale = 2)
    private BigDecimal cpiGradePoints;

    @Column(name = "ssrcpiregisteredcredits", precision = 38, scale = 2)
    private BigDecimal cpiRegisteredCredits;

    @Column(name = "ssrspi", length = 255)
    private String spi;

    @Column(name = "ssrcpi", length = 255)
    private String cpi;

    @Column(name = "ssrcreatedby")
    private Long createdBy;

    @Column(name = "ssrcreatedat")
    private LocalDateTime createdAt;

    @Column(name = "ssrlastupdatedby")
    private Long lastUpdatedBy;

    @Column(name = "ssrlastupdatedat")
    private LocalDateTime lastUpdatedAt;

    @Column(name = "ssrrowstate")
    private Long rowState;

    @Column(name = "ssrcpi_numeric")
    private BigDecimal cpiNumeric;

    @Column(name = "ssrspi_numeric")
    private BigDecimal spiNumeric;




    // Getters and Setters

    public BigDecimal getCpiNumeric() {
        return cpiNumeric;
    }

    public void setCpiNumeric(BigDecimal cpiNumeric) {
        this.cpiNumeric = cpiNumeric;
    }

    public BigDecimal getSpiNumeric() {
        return spiNumeric;
    }

    public void setSpiNumeric(BigDecimal spiNumeric) {
        this.spiNumeric = spiNumeric;
    }

    public StudentGrade getGrade() {
        return grade;
    }
    
    public void setGrade(StudentGrade grade) {
        this.grade = grade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentRegistrations getStudentRegistration() {
        return studentRegistration;
    }

    public void setStudentRegistration(StudentRegistrations studentRegistration) {
        this.studentRegistration = studentRegistration;
    }

    public BigDecimal getCreditsRegistered() {
        return creditsRegistered;
    }

    public void setCreditsRegistered(BigDecimal creditsRegistered) {
        this.creditsRegistered = creditsRegistered;
    }

    public BigDecimal getCreditsEarned() {
        return creditsEarned;
    }

    public void setCreditsEarned(BigDecimal creditsEarned) {
        this.creditsEarned = creditsEarned;
    }

    public BigDecimal getGradePointsEarned() {
        return gradePointsEarned;
    }

    public void setGradePointsEarned(BigDecimal gradePointsEarned) {
        this.gradePointsEarned = gradePointsEarned;
    }

    public BigDecimal getCumCreditsRegistered() {
        return cumCreditsRegistered;
    }

    public void setCumCreditsRegistered(BigDecimal cumCreditsRegistered) {
        this.cumCreditsRegistered = cumCreditsRegistered;
    }

    public BigDecimal getCumCreditsEarned() {
        return cumCreditsEarned;
    }

    public void setCumCreditsEarned(BigDecimal cumCreditsEarned) {
        this.cumCreditsEarned = cumCreditsEarned;
    }

    public BigDecimal getCumGradePointsEarned() {
        return cumGradePointsEarned;
    }

    public void setCumGradePointsEarned(BigDecimal cumGradePointsEarned) {
        this.cumGradePointsEarned = cumGradePointsEarned;
    }

    public BigDecimal getSpiGradePoints() {
        return spiGradePoints;
    }

    public void setSpiGradePoints(BigDecimal spiGradePoints) {
        this.spiGradePoints = spiGradePoints;
    }

    public BigDecimal getCpiGradePoints() {
        return cpiGradePoints;
    }

    public void setCpiGradePoints(BigDecimal cpiGradePoints) {
        this.cpiGradePoints = cpiGradePoints;
    }

    public BigDecimal getCpiRegisteredCredits() {
        return cpiRegisteredCredits;
    }

    public void setCpiRegisteredCredits(BigDecimal cpiRegisteredCredits) {
        this.cpiRegisteredCredits = cpiRegisteredCredits;
    }

    public String getSpi() {
        return spi;
    }

    public void setSpi(String spi) {
        this.spi = spi;
    }

    public String getCpi() {
        return cpi;
    }

    public void setCpi(String cpi) {
        this.cpi = cpi;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public Long getRowState() {
        return rowState;
    }

    public void setRowState(Long rowState) {
        this.rowState = rowState;
    }
}
