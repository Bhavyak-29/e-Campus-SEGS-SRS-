package com.segs.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
public class Users {

    @Id
    private int userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false, unique = true)
    private String userMailId;

    @Column(nullable = false)
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
