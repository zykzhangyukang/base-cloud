package com.coderman.auth.websocket;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Administrator
 */
public enum WebSocketChannelEnum {
    //测试使用的简易点对点聊天
    CHAT("CHAT", "测试使用的简易点对点聊天", "/topic/reply"),

    TOPIC_SYSTEM("TOPIC_SYSTEM", "系统通知", "/topic/sysMsg");

    WebSocketChannelEnum(String code, String description, String subscribeUrl) {
        this.code = code;
        this.description = description;
        this.subscribeUrl = subscribeUrl;
    }

    /**
     * 唯一CODE
     */
    private String code;
    /**
     * 描述
     */
    private String description;
    /**
     * WebSocket客户端订阅的URL
     */
    private String subscribeUrl;

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getSubscribeUrl() {
        return subscribeUrl;
    }

    /**
     * 通过CODE查找枚举类
     */
    public static WebSocketChannelEnum fromCode(String code){
        if(StringUtils.isNoneBlank(code)){
            for(WebSocketChannelEnum channelEnum : values()){
                if(channelEnum.code.equals(code)){
                    return channelEnum;
                }
            }
        }

        return null;
    }

}