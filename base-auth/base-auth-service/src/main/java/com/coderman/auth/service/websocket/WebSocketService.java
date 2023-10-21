package com.coderman.auth.service.websocket;

/**
 * 消息通知
 *
 * @author ：zhangyukang
 * @date ：2023/10/20 10:17
 */
public interface WebSocketService {



    /**
     * websocket给指定的用户推送消息
     *
     * @param senderId   发送人id
     * @param receiverId 接受人id
     * @param payload    消息内容
     */
    public void sendToUser(Integer senderId, Integer receiverId, Object payload);
}
