package com.segs.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users", schema = "ec2")
public class Users {

    @Id
    @Column(name= "uid")
    private int userId;

    @Column(name= "uname")
    private String userName;

    @Column(name="uemail")
    private String userMailId;

    @Column(name="urole_0")
    private String userCategory;

    // Constructors
    public Users() {
    }

    public Users(String userName, String userMailId, String userCategory, int userId) {
        this.userName = userName;
        this.userMailId = userMailId;
        this.userCategory = userCategory;
        this.userId = userId;
    }

    // Getters and Setters
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMailId() {
        return userMailId;
    }
    public void setUserMailId(String userMailId) {
        this.userMailId = userMailId;
    }

    public String getUserCategory() {
        return userCategory;
    }
    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
