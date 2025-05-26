package com.segs.demo.model;

public class User {
    private String UserName;
    private String UserMailId;
    private String UserCategory;
    private int userId;
    public User(String userName, String userMailId, String userCategory, int userId) {
        UserName = userName;
        UserMailId = userMailId;
        UserCategory = userCategory;
        this.userId = userId;
    }
    public String getUserName() {
        return UserName;
    }
    public void setUserName(String userName) {
        UserName = userName;
    }
    public String getUserMailId() {
        return UserMailId;
    }
    public void setUserMailId(String userMailId) {
        UserMailId = userMailId;
    }
    public String getUserCategory() {
        return UserCategory;
    }
    public void setUserCategory(String userCategory) {
        UserCategory = userCategory;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
