package com.coderman.auth.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Nonnull;
import javax.annotation.Resource;

@Component
public class MyWebSocketHandler implements WebSocketHandler {

    @Resource
    private WebSocketSessionManager webSocketSessionManager;

    private String getUserIdFromSession(WebSocketSession session) {
        return "user123";
    }

    /**
     * 连接建立时的处理逻辑
     * @param webSocketSession
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(@Nonnull WebSocketSession webSocketSession) throws Exception {

        this.webSocketSessionManager.addSession(this.getUserIdFromSession(webSocketSession), webSocketSession);
    }

    @Override
    public void handleMessage(@Nonnull WebSocketSession webSocketSession,@Nonnull WebSocketMessage<?> webSocketMessage) throws Exception {
    }

    /**
     * 连接关闭时的处理逻辑
     * @param webSocketSession
     * @param throwable
     * @throws Exception
     */
    @Override
    public void handleTransportError(@Nonnull WebSocketSession webSocketSession, @Nonnull Throwable throwable) throws Exception {
    }

    /**
     * 处理传输错误
     * @param webSocketSession
     * @param closeStatus
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(@Nonnull WebSocketSession webSocketSession,@Nonnull CloseStatus closeStatus) throws Exception {
        this.webSocketSessionManager.removeSession(this.getUserIdFromSession(webSocketSession) , webSocketSession);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
