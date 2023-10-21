package com.coderman.gateway.config;

import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.DefaultCorsProcessor;
import org.springframework.web.server.ServerWebExchange;


/**
 * 用于解决spring websocket 跨域添加重复响应头的bug
 */
public class CorsProcessor extends DefaultCorsProcessor {

    @Override
    public boolean process(CorsConfiguration config, ServerWebExchange exchange) {

        if (isWebSocketReq(exchange)) {
            return true;
        }

        return super.process(config, exchange);
    }

    /**
     * 判断是否是 webSocket的连接请求
     *
     * @param exchange
     * @return
     */
    public static boolean isWebSocketReq(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        RequestPath path = request.getPath();
        String realPath = path.pathWithinApplication().value();
        AntPathMatcher matcher = new AntPathMatcher();
        String pattern = "/sys_websocket/**";
        return matcher.match(pattern, realPath);
    }

}
