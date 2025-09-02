package com.expenditure_diary.expenditure_diary.module.authentication.dto.resp;

public class LoginResp extends RegisterResp{
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
