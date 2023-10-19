package com.coderman.auth.constant;

/**
 *  redis发布订阅主题
 * @author zhangyukang
 */
public interface RedisConstant {


    /**
     * 刷新系统资源
     */
    public static final String CHANNEL_REFRESH_RESC = "TOPIC:REFRESH_RESC";

    /**
     * 存储websocket连接的用户
     */
    public static final String WEBSOCKET_USER_SET = "websocket.user.set";
}
