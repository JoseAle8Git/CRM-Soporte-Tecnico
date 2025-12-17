package com.crm.crmSoporteTecnico.components;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtBlacklistFilter extends OncePerRequestFilter {

    private final StringRedisTemplate redisTemplate;

    public JwtBlacklistFilter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = null;
        if(request.getCookies() != null) {
            for(Cookie c : request.getCookies()) {
                if("jwt".equals(c.getName())) {
                    token = c.getValue();
                    break;
                }
            }
        }

        if(token != null && Boolean.TRUE.equals(redisTemplate.hasKey(token))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token invalidado (Logout realizado).");
            return;
        }
        filterChain.doFilter(request,response);
    }

}
