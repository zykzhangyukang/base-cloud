package com.coderman.gateway.config;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 配置跨域
 */
@Component
public class CORSFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.addHeader("Access-Control-Allow-Credentials", "true");
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        res.addHeader("Access-Control-Allow-Headers", "Content-Type,X-CAF-Authorization-Token,sessionToken,X-TOKEN,Authorization");

        HttpServletRequest httpServletRequest = ((HttpServletRequest) request);
        if (StringUtils.equalsIgnoreCase("OPTIONS", httpServletRequest.getMethod())) {
            response.getWriter().println("ok");
            return;
        }

        chain.doFilter(request, response);
    }

}

