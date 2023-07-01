package com.coderman.auth.api.user.impl;

import com.coderman.api.vo.ResultVO;
import com.coderman.auth.api.UserApi;
import com.coderman.auth.service.user.UserService;
import com.coderman.auth.vo.user.AuthUserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserApiImpl implements UserApi {

    @Resource
    private UserService userService;


    @Override
    public ResultVO<AuthUserVO> getUserByToken(String token) {
        return this.userService.getUserByToken(token);
    }
}
