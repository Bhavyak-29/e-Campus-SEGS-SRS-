package com.ec2.main.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@IdClass(WorkTrailId.class)
@Table(name="work_trail", schema="ec2")
public class work_trail {
    @Id
    @Column(name= "work_id")
    private Long work_id;

    @Column(name= "work_type_code")
    private Long work_type_code;

    @Id
    @Column(name= "node_number")
    private Long node_number;

    @Column(name= "employee_id")
    private Long employee_id;

    @Id
    @Column(name= "iteration_number")
    private Long interation_number;

    @Column(name= "response_code")
    private Long response_code;

    @Column(name= "response_date")
    private LocalDateTime response_date;

    @Column(name= "remarks")
    private String remarks;

    @Column(name= "prev_node_number")
    private Long prev_node_number;

    @Column(name= "prev_iteration_number")
    private Long prev_iteration_number;

    @Column(name= "prev_employee_id")
    private Long prev_employee_id;
    

    public Long getWork_id() {
        return work_id;
    }

    public void setWork_id(Long work_id) {
        this.work_id = work_id;
    }

    public Long getWork_type_code() {
        return work_type_code;
    }

    public void setWork_type_code(Long work_type_code) {
        this.work_type_code = work_type_code;
    }

    public Long getNode_number() {
        return node_number;
    }

    public void setNode_number(Long node_number) {
        this.node_number = node_number;
    }

    public Long getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Long employee_id) {
        this.employee_id = employee_id;
    }

    public Long getInteration_number() {
        return interation_number;
    }

    public void setInteration_number(Long interation_number) {
        this.interation_number = interation_number;
    }

    public Long getResponse_code() {
        return response_code;
    }

    public void setResponse_code(Long response_code) {
        this.response_code = response_code;
    }

    public LocalDateTime getResponse_date() {
        return response_date;
    }

    public void setResponse_date(LocalDateTime response_date) {
        this.response_date = response_date;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getPrev_node_number() {
        return prev_node_number;
    }

    public void setPrev_node_number(Long prev_node_number) {
        this.prev_node_number = prev_node_number;
    }

    public Long getPrev_iteration_number() {
        return prev_iteration_number;
    }

    public void setPrev_iteration_number(Long prev_iteration_number) {
        this.prev_iteration_number = prev_iteration_number;
    }

    public Long getPrev_employee_id() {
        return prev_employee_id;
    }

    public void setPrev_employee_id(Long prev_employee_id) {
        this.prev_employee_id = prev_employee_id;
    }

}
