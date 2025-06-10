package com.segs.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User2 {
    @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
    private Long studentid;

    public Long getStudentid() {
        return studentid;
    }
    public void setStudentid(Long studentid) {
        this.studentid = studentid;
    }
    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
    private String grade;
    public User2(){

    }
    public User2(Long studentid, String grade) {
        this.studentid = studentid;
        this.grade = grade;
    }
    

}
