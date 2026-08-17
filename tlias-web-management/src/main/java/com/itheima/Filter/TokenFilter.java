package com.itheima.Filter;

import com.itheima.utils.JwtUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class TokenFilter implements  Filter{
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException{
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
//        获取请求路径
        String requestURI = req.getRequestURI();
        if (requestURI.contains("login")){
            log.info("登录请求，放行");
            chain.doFilter(req, resp);
            return ;
        } else {
            String token = req.getHeader("token");
            if (token == null || token.isEmpty()){
                log.info("未登录，拦截");
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        try {
            JwtUtils.parseJWT(token);
        } catch (Exception e) {
            log.info("未登录，拦截");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        log.info("已登录，放行");
        chain.doFilter(req, resp);
        }
    }
}
