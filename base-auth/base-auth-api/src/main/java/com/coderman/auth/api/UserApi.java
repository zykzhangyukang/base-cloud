package com.coderman.auth.api;

import com.coderman.api.vo.ResultVO;
import com.coderman.auth.vo.user.AuthUserVO;

public interface UserApi {


    /**
     * 根据token获取用户信息
     * @param token token
     * @return
     */
    ResultVO<AuthUserVO> getUserByToken(String token);

}
