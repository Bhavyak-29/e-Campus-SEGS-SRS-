package com.ec2.main.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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
    private Short nodeNumber;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "iteration_number", nullable = false)
    private Short iterationNumber;

    @Column(name = "response_code", nullable = false)
    private Short responseCode;

    @Column(name = "response_date")
    private LocalDateTime responseDate;

    @Column(name = "remarks", length = 1000)
    private String remarks;

    @Column(name = "prev_node_number")
    private Short prevNodeNumber;

    @Column(name = "prev_iteration_number")
    private Short prevIterationNumber;

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

    public Short getNodeNumber() {
        return nodeNumber;
    }

    public void setNodeNumber(Short nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Short getIterationNumber() {
        return iterationNumber;
    }

    public void setIterationNumber(Short iterationNumber) {
        this.iterationNumber = iterationNumber;
    }

    public Short getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Short responseCode) {
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

    public Short getPrevNodeNumber() {
        return prevNodeNumber;
    }

    public void setPrevNodeNumber(Short prevNodeNumber) {
        this.prevNodeNumber = prevNodeNumber;
    }

    public Short getPrevIterationNumber() {
        return prevIterationNumber;
    }

    public void setPrevIterationNumber(Short prevIterationNumber) {
        this.prevIterationNumber = prevIterationNumber;
    }

    public Users getPrevEmployee() {
        return prevEmployee;
    }

    public void setPrevEmployee(Users prevEmployee) {
        this.prevEmployee = prevEmployee;
    }
}
