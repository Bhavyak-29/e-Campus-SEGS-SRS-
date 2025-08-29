package com.ec2.main.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


//toupdate

@Entity
@Table(name = "work_trail", schema = "ec2")
public class WorkTrail {

    @Id
    @Column(name = "work_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_type_code", referencedColumnName = "code", nullable = false)
    private WorkType workType;

    @Column(name = "node_number", nullable = false)
    private Long nodeNumber;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "iteration_number", nullable = false)
    private Long iterationNumber;

    @Column(name = "response_code", nullable = false)
    private Long responseCode;

    @Column(name = "response_date")
    private LocalDateTime responseDate;

    @Column(name = "remarks", length = 1000)
    private String remarks;

    @Column(name = "prev_node_number")
    private Long prevNodeNumber;

    @Column(name = "prev_iteration_number")
    private Long prevIterationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prev_employee_id")
    private Users prevEmployee;

    // Getters and setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkType getWorkType() {
        return workType;
    }

    public void setWorkType(WorkType workType) {
        this.workType = workType;
    }

    public Long getNodeNumber() {
        return nodeNumber;
    }

    public void setNodeNumber(Long nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getIterationNumber() {
        return iterationNumber;
    }

    public void setIterationNumber(Long iterationNumber) {
        this.iterationNumber = iterationNumber;
    }

    public Long getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Long responseCode) {
        this.responseCode = responseCode;
    }

    public LocalDateTime getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(LocalDateTime responseDate) {
        this.responseDate = responseDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getPrevNodeNumber() {
        return prevNodeNumber;
    }

    public void setPrevNodeNumber(Long prevNodeNumber) {
        this.prevNodeNumber = prevNodeNumber;
    }

    public Long getPrevIterationNumber() {
        return prevIterationNumber;
    }

    public void setPrevIterationNumber(Long prevIterationNumber) {
        this.prevIterationNumber = prevIterationNumber;
    }

    public Users getPrevEmployee() {
        return prevEmployee;
    }

    public void setPrevEmployee(Users prevEmployee) {
        this.prevEmployee = prevEmployee;
    }
}
