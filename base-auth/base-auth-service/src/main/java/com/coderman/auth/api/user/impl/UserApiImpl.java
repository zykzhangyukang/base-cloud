package com.coderman.auth.api.user.impl;

import com.coderman.api.vo.ResultVO;
import com.coderman.auth.api.UserApi;
import com.coderman.auth.service.user.UserService;
import com.coderman.auth.vo.user.AuthUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserApiImpl implements UserApi {

    @Autowired
    private UserService userService;


    @Override
    public ResultVO<AuthUserVO> getUserByToken(String token) {
        return this.userService.getUserByToken(token);
    }
}