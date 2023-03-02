package com.toy.overall_practice.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            //TODO
            if (request.getServletPath().startsWith("not yet")) { // 토큰을 재발급하는 API 경우 토큰 체크 로직 건너뛰기
                filterChain.doFilter(request, response);
            } else {
                String accessToken = jwtTokenProvider.resolveAccessToken(request);
                boolean isTokenValid = jwtTokenProvider.validateToken(accessToken);

                if (StringUtils.hasText(accessToken) && isTokenValid) {
                    setAuthentication(accessToken);
                }

                filterChain.doFilter(request, response);
            }

        } catch (ExpiredJwtException e) {
            log.info("ExpiredJwtException = {}", "만료된 토큰입니다.", e);
        }
    }

    private void setAuthentication(String token) {
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
