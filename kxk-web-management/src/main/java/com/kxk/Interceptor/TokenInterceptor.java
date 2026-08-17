package com.kxk.Interceptor;

import com.kxk.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
//        获取请求路径
        String requestURI = req.getRequestURI();
        if (requestURI.contains("login")){
            log.info("登录请求，放行");
            return true;
        } else {
            String token = req.getHeader("token");
            if (token == null || token.isEmpty()){
                log.info("未登录，拦截");
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            try {
                JwtUtils.parseJWT(token);
            } catch (Exception e) {
                log.info("未登录，拦截");
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return  false;
            }
            log.info("已登录，放行");
            return  true;
        }


    }
}
