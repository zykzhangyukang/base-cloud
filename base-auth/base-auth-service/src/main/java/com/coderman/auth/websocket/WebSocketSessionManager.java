package com.coderman.auth.websocket;

import com.coderman.service.redis.RedisService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Component
public class WebSocketSessionManager {

    @Resource
    private RedisService redisService;

    private static final String REDIS_WEBSOCKET_KEY_PREFIX = "websocket-session:";
    private static final Integer REDIS_WEBSOCKET_DB = 5;

    public void addSession(String userId, WebSocketSession session) {
        String sessionId = session.getId();
        String redisKey = getRedisKey(userId);
        this.redisService.setHash(redisKey, sessionId, session, REDIS_WEBSOCKET_DB);
    }

    public void removeSession(String userId, WebSocketSession session) {
        String sessionId = session.getId();
        String redisKey = getRedisKey(userId);
        List<String> fields = Collections.singletonList(sessionId);
        this.redisService.delHash(redisKey, fields, REDIS_WEBSOCKET_DB);
    }

    public WebSocketSession getSession(String userId, String sessionId) {
        String redisKey = getRedisKey(userId);
        return (WebSocketSession) this.redisService.getHash(redisKey, sessionId, WebSocketSession.class, REDIS_WEBSOCKET_DB);
    }

    private String getRedisKey(String userId) {
        return REDIS_WEBSOCKET_KEY_PREFIX + userId;
    }

}
