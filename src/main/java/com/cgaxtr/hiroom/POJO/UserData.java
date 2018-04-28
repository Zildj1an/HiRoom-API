package com.cgaxtr.hiroom.POJO;

public class UserData {
    private String token;
    private User user;

    public UserData(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {

        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
