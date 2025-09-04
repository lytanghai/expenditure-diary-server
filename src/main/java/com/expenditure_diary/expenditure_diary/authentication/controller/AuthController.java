package com.expenditure_diary.expenditure_diary.authentication.controller;

import com.expenditure_diary.expenditure_diary.authentication.dto.req.LoginReq;
import com.expenditure_diary.expenditure_diary.authentication.dto.req.RegisterReq;
import com.expenditure_diary.expenditure_diary.authentication.dto.resp.LoginResp;
import com.expenditure_diary.expenditure_diary.authentication.dto.resp.RegisterResp;
import com.expenditure_diary.expenditure_diary.authentication.service.AuthenticationService;
import com.expenditure_diary.expenditure_diary.dto.resp.CommonResp;
import com.expenditure_diary.expenditure_diary.authentication.dto.req.ChangePasswordReq;
import com.expenditure_diary.expenditure_diary.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "${backend_server.web_url}", allowedHeaders = "*")
public class AuthController {

    @Autowired private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseBuilder<RegisterResp> register(@RequestBody RegisterReq req) {
        return authenticationService.register(req);
    }

    @PostMapping("/login")
    public ResponseBuilder<LoginResp> login(@RequestBody LoginReq login) throws InterruptedException {
        return authenticationService.login(login);
    }

    @PostMapping("/change-password")
    public ResponseBuilder<CommonResp> changePassword(@RequestBody ChangePasswordReq req) {
        return authenticationService.changePassword(req);
    }

}