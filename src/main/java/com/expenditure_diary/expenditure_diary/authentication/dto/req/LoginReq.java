package com.expenditure_diary.expenditure_diary.authentication.dto.req;

public class LoginReq extends RegisterReq{
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
