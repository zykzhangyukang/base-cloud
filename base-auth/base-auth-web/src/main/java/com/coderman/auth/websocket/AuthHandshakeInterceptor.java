package com.coderman.auth.websocket;

import com.alibaba.fastjson.JSON;
import com.coderman.api.constant.CommonConstant;
import com.coderman.api.constant.RedisDbConstant;
import com.coderman.api.constant.ResultConstant;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.constant.RedisConstant;
import com.coderman.erp.api.UserApi;
import com.coderman.erp.vo.AuthUserVO;
import com.coderman.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;

/**
 * 建立连接时候的权限拦截器
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class AuthHandshakeInterceptor implements ChannelInterceptor {

    @Resource
    private UserApi userApi;

    @Resource
    private RedisService redisService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        // 不是第一次连接
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null || !StompCommand.CONNECT.equals(accessor.getCommand())) {

            return message;
        }

        List<String> nativeHeader = accessor.getNativeHeader(CommonConstant.USER_TOKEN_NAME);
        if (CollectionUtils.isEmpty(nativeHeader)) {

            log.error("未登录系统，禁止连接WebSocket!");
            return null;
        }

        String token = nativeHeader.get(0);

        ResultVO<AuthUserVO> resultVO = this.userApi.getUserByToken(token);
        if (!ResultConstant.RESULT_CODE_200.equals(resultVO.getCode())) {
            log.error("未登录系统，禁止连接WebSocket! , resultVO:{}", JSON.toJSON(resultVO));
            return null;
        }

        AuthUserVO authUserVO = resultVO.getResult();
        if (this.redisService.isSetMember(RedisConstant.WEBSOCKET_USER_SET, authUserVO.getUsername(), RedisDbConstant.REDIS_DB_DEFAULT)) {
            log.error("同一个用户不准建立多个连接WebSocket");
            return null;
        }

        // 单节点会话
        accessor.setUser(new MyPrincipal(authUserVO.getUsername()));

        // 将用户名存到Redis中
        this.redisService.addToSet(RedisConstant.WEBSOCKET_USER_SET, authUserVO.getUsername(), RedisDbConstant.REDIS_DB_DEFAULT);

        log.info(MessageFormat.format("用户{0}请求建立WebSocket连接", authUserVO.getUsername()));

        return message;
    }

}
