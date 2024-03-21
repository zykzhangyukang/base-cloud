package com.coderman.auth.service.websocket.impl;

import com.alibaba.fastjson.JSON;
import com.coderman.api.constant.RedisDbConstant;
import com.coderman.auth.constant.RedisConstant;
import com.coderman.auth.constant.WebSocketChannelEnum;
import com.coderman.auth.dto.websocket.WebsocketRedisMsg;
import com.coderman.auth.service.websocket.WebSocketService;
import com.coderman.redis.annotaion.RedisChannelListener;
import com.coderman.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class WebSocketServiceImpl implements WebSocketService {

    @Resource
    private SimpUserRegistry simpUserRegistry;

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @Resource
    private RedisService redisService;

    /**
     * 发送消息到用户的方法
     *
     * @param senderId   发送方id
     * @param receiverId 接收方id
     * @param payload    消息内容
     * @return void
     */
    @Override
    public void sendToUser(Integer senderId, Integer receiverId, Object payload) {

        String sender = String.valueOf(senderId);
        String receiver = String.valueOf(receiverId);
        String destination = String.format(WebSocketChannelEnum.USER_SYS_MSG.getSubscribeUrl(), receiverId);
        SimpUser simpUser = simpUserRegistry.getUser(receiver);

        //如果接收者存在，则发送消息
        if (simpUser != null && StringUtils.isNoneBlank(simpUser.getName())) {

            simpMessagingTemplate.convertAndSend(destination, payload);
            log.info("sendToUser-websocket推送消息 destination => {} ,payload => {}", destination, JSON.toJSONString(payload));
        }
        //如果接收者在线，则说明接收者连接了集群的其他节点，需要通知接收者连接的那个节点发送消息
        else if (redisService.isSetMember(RedisConstant.WEBSOCKET_USER_SET, receiver, RedisDbConstant.REDIS_DB_DEFAULT)) {

            WebsocketRedisMsg<Object> websocketRedisMsg = new WebsocketRedisMsg<>(receiver, destination, payload);
            redisService.sendMessage(RedisConstant.CHANNEL_WEBSOCKET_NOTIFY, websocketRedisMsg);
        }
        //否则将消息存储到redis，等用户上线后主动拉取未读消息
        else {

            String listKey = RedisConstant.REDIS_UNREAD_MSG_PREFIX + ":" + receiver + ":" + destination;
            log.info("消息接收者:{}还未建立WebSocket连接，{} 发送的消息【{}】将被存储到Redis的【{}】列表中", receiver, sender, payload, listKey);
            this.redisService.setListAppend(listKey, payload, RedisDbConstant.REDIS_DB_DEFAULT);
        }
    }


    /**
     * 订阅redis主题，解决分布式多节点websocket连接问题。
     *
     * @param websocketRedisMsg 消息内容
     */
    @RedisChannelListener(channelName = RedisConstant.CHANNEL_WEBSOCKET_NOTIFY, clazz = WebsocketRedisMsg.class)
    public void handWebSocketNotify(WebsocketRedisMsg<Object> websocketRedisMsg) {

        log.info("Received Message: {}", JSON.toJSONString(websocketRedisMsg));
        String receiver = websocketRedisMsg.getReceiver();
        Object content = websocketRedisMsg.getContent();
        String destination = websocketRedisMsg.getDestination();

        // 取出用户名并判断是否连接到当前应用节点的WebSocket
        SimpUser simpUser = simpUserRegistry.getUser(receiver);
        if (simpUser != null && StringUtils.isNoneBlank(simpUser.getName()) && StringUtils.isNotBlank(destination)) {

            //  给WebSocket客户端发送消息
            simpMessagingTemplate.convertAndSend(destination, content);
            log.info("handWebSocketNotify-websocket推送消息 destination => {} ,payload => {}", destination, JSON.toJSONString(content));
        }
    }
}
