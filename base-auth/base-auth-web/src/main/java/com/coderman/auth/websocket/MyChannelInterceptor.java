package com.coderman.auth.websocket;

import com.coderman.api.constant.RedisDbConstant;
import com.coderman.auth.constant.RedisConstant;
import com.coderman.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.text.MessageFormat;

/**
 * @author ：zhangyukang
 * @date ：2023/10/19 14:39
 */
@Component
public class MyChannelInterceptor implements ChannelInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private RedisService redisService;


    @Override
    public void afterSendCompletion(@NotNull Message<?> message, @NotNull MessageChannel channel, boolean sent, Exception ex) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = accessor.getSessionId();
        StompCommand command = accessor.getCommand();

        //用户已经断开连接
        if (StompCommand.DISCONNECT.equals(command)) {

            String user;
            Principal principal = accessor.getUser();
            if (principal instanceof MyPrincipal) {

                // 将会话信息从redis中移除
                MyPrincipal myPrincipal = (MyPrincipal) principal;
                user = String.valueOf(myPrincipal.getUserId());
                redisService.removeFromSet(RedisConstant.WEBSOCKET_USER_SET, user, RedisDbConstant.REDIS_DB_DEFAULT);

            } else {

                user = sessionId;
            }

            logger.info("用户:{} 的WebSocket连接已经断开,sessionId:{}", user, sessionId);
        }
    }

}
