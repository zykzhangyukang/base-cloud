package com.coderman.auth.websocket;

import java.security.Principal;

/**
 * @author ：zhangyukang
 * @date ：2023/10/19 14:45
 */
public class MyPrincipal implements Principal {

    private final String loginName;

    public MyPrincipal(String loginName) {
        this.loginName = loginName;
    }

    @Override
    public String getName() {
        return loginName;
    }
}