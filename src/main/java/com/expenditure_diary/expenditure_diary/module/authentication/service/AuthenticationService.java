package com.expenditure_diary.expenditure_diary.module.authentication.service;

import com.expenditure_diary.expenditure_diary.constant.Constant;
import com.expenditure_diary.expenditure_diary.dto.resp.CommonResp;
import com.expenditure_diary.expenditure_diary.exception.ServiceException;
import com.expenditure_diary.expenditure_diary.module.activity_log.service.ActivityLogService;
import com.expenditure_diary.expenditure_diary.module.authentication.dto.req.ChangePasswordReq;
import com.expenditure_diary.expenditure_diary.module.authentication.dto.req.LoginReq;
import com.expenditure_diary.expenditure_diary.module.authentication.dto.req.RegisterReq;
import com.expenditure_diary.expenditure_diary.module.authentication.dto.resp.LoginResp;
import com.expenditure_diary.expenditure_diary.module.authentication.dto.resp.RegisterResp;
import com.expenditure_diary.expenditure_diary.module.model.secondary.UserEntity;
import com.expenditure_diary.expenditure_diary.module.repository.primary.UserRepository;
import com.expenditure_diary.expenditure_diary.util.DateUtil;
import com.expenditure_diary.expenditure_diary.util.JwtUtil;
import com.expenditure_diary.expenditure_diary.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Forgot password
 * */
@Service
public class AuthenticationService {

    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder encoder;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private ActivityLogService activityLogService;

    private static final String USER_MUST_ENTER_PWD = "User must enter password!";
    private static final String USER_MUST_ENTER_OLD_PWD = "User must enter old password!";
    private static final String USER_MUST_ENTER_NEW_PWD = "User must enter new password!";
    private static final String USER_MUST_ENTER_USR = "User must enter password!";
    private static final String USER_MUST_ENTER_EML = "User must enter email!";
    private static final String USER_MUST_ENTER_USR_EML = "User must enter username or email!";
    private static final String USER_EML_EXIST = "Username or Email is already exist! Please try a different one!";
    private static final String INVALID_CREDENTIAL = "Invalid Credential!";
    private static final String USER_LOGIN = "USER_LOGIN";
    private static final String USER_REGISTER = "USER_REGISTER";
    private static final String USER_CHANGE_PWD = "USER_CHANGE_PWD";

    private static Long userId = 0L;

    private void validate(String type, String username, String password, String email) {
        if("R".equals(type)) {
            if(password.isEmpty())
                throw new ServiceException(Constant.FATAL_ERROR, USER_MUST_ENTER_PWD);

            if(username.isEmpty())
                throw new ServiceException(Constant.FATAL_ERROR, USER_MUST_ENTER_USR);

            if(email.isEmpty())
                throw new ServiceException(Constant.FATAL_ERROR, USER_MUST_ENTER_EML);

        } else {
            if(username.isEmpty() && email.isEmpty())
                throw new ServiceException(Constant.FATAL_ERROR, USER_MUST_ENTER_USR_EML);
        }
    }

    private String validate(String username, String email) {
        if(username.isEmpty()) {
            return email;
        } else {
            return username;
        }
    }

    private void validate(String email, String oldPwd, String newPwd) {
        if(email.isEmpty())
            throw new ServiceException(Constant.FATAL_ERROR, USER_MUST_ENTER_EML);

        if(oldPwd.isEmpty())
            throw new ServiceException(Constant.FATAL_ERROR, USER_MUST_ENTER_OLD_PWD);

        if(newPwd.isEmpty())
            throw new ServiceException(Constant.FATAL_ERROR, USER_MUST_ENTER_NEW_PWD);

    }
    public ResponseBuilder<RegisterResp> register(RegisterReq registerReq) {

        String username = registerReq.getUsername();
        String email = registerReq.getEmail();
        String password = registerReq.getPassword();

        validate("R" ,username, password, email);

        RegisterResp response = new RegisterResp();

        try {
            List<UserEntity> findByUsernameOrEmail = userRepo.findByUsernameOrEmail(username, email);

            if(!findByUsernameOrEmail.isEmpty())
                throw new ServiceException(Constant.UNEXPECTED_ERROR, USER_EML_EXIST);

            UserEntity newUser = new UserEntity();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPassword(encoder.encode(registerReq.getPassword()));
            newUser.setCreatedAt(LocalDateTime.now());

            userRepo.save(newUser);
            response.setResult(Constant.SUCCESS);
            response.setEmail(email);
            response.setCreatedAtStr(DateUtil.format(newUser.getCreatedAt().toString()));
            response.setUsername(username);

            userId = newUser.getId();

            activityLogService.insert(USER_REGISTER, newUser.getId(), Constant.SUCCESS, Constant.SUCCESS);

        }catch (ServiceException exception) {
            response.setResult(Constant.UNEXPECTED_ERROR);
            response.setDetail(exception.getMessage());
            activityLogService.insert(USER_REGISTER, userId, Constant.UNEXPECTED_ERROR, exception.getMessage());
        }

        return ResponseBuilder.success(response);
    }

    public ResponseBuilder<LoginResp> login(LoginReq loginReq) throws InterruptedException {
        String username = loginReq.getUsername();
        String email = loginReq.getEmail();
        String password = loginReq.getPassword();
        validate("L", username, password, email);
        LoginResp response = new LoginResp();

        try {
            UserEntity user = userRepo.findByUsername(validate(username, email)).orElse(null);
            if(user == null)
                throw new ServiceException(Constant.GENERAL_ERROR, "User: "+ username +" is not found!");

            userId = user.getId();

            if(encoder.matches(password, user.getPassword())) {
                response.setResult(Constant.SUCCESS);
                response.setToken(jwtUtil.generateToken(username, userId));
                response.setUsername(username);
                response.setEmail(email);
                response.setCreatedAtStr(DateUtil.format(user.getCreatedAt().toString()));
                activityLogService.insert(USER_LOGIN, userId, Constant.SUCCESS, Constant.SUCCESS);
            } else {
                response.setResult(Constant.GENERAL_ERROR);
                response.setDetail(INVALID_CREDENTIAL);
                activityLogService.insert(USER_LOGIN, userId, Constant.GENERAL_ERROR, INVALID_CREDENTIAL);
            }

        }catch (ServiceException e) {
            response.setResult(Constant.UNEXPECTED_ERROR);
            activityLogService.insert(USER_LOGIN, userId, Constant.UNEXPECTED_ERROR, e.getMessage());
        }

        return ResponseBuilder.success(response);
    }

    public ResponseBuilder<CommonResp> changePassword(ChangePasswordReq req) {
        String email = req.getEmail();
        String oldPwd = req.getOldPassword();
        String newPwd = req.getNewPassword();

        validate(email, oldPwd, newPwd);

        CommonResp response = new CommonResp();
        try {
            UserEntity user = userRepo.findByEmail(email);

            if(user == null)
                throw new ServiceException(Constant.GENERAL_ERROR, "Email: "+ email +" is not found!");

            userId = user.getId();

            if(encoder.matches(oldPwd, user.getPassword())) {
                user.setPassword(encoder.encode(newPwd));
                user.setUpdatedAt(LocalDateTime.now());
                userRepo.save(user);

                response.setResult(Constant.SUCCESS);
                activityLogService.insert(USER_CHANGE_PWD, userId, Constant.SUCCESS, Constant.SUCCESS);
            } else {
                activityLogService.insert(USER_CHANGE_PWD, userId, Constant.UNEXPECTED_ERROR, "Old Password is not valid!");
                throw new ServiceException(Constant.GENERAL_ERROR, "Old Password is not valid!");
            }
        }catch (ServiceException e) {
            response.setResult(Constant.UNEXPECTED_ERROR);
            response.setDetail(e.getMessage());
            activityLogService.insert(USER_CHANGE_PWD, userId, Constant.UNEXPECTED_ERROR, e.getMessage());
        }

        return ResponseBuilder.success(response);
    }
}
