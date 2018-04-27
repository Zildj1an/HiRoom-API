package com.cgaxtr.hiroom.POJO;

public class Token {
    private String JWT;

    public String getJWT() {
        return JWT;
    }

    public void setJWT(String JWT) {
        this.JWT = JWT;
    }

    public Token(String JWT) {

        this.JWT = JWT;
    }
}
