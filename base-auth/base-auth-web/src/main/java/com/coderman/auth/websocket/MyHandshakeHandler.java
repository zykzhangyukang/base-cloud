package com.coderman.auth.websocket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.Map;

/**
 * @author ：zhangyukang
 * @date ：2023/10/19 12:02
 */
@Component
public class MyHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(@NotNull ServerHttpRequest request, @NotNull WebSocketHandler wsHandler, @NotNull Map<String, Object> attributes) {

        return super.determineUser(request, wsHandler,attributes);
    }
}