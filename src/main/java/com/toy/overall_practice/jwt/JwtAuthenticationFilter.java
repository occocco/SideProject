package com.toy.overall_practice.jwt;

import com.toy.overall_practice.exception.exhandler.ExResult;
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

import static java.util.Arrays.stream;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperty jwtProperty;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!StringUtils.hasText(request.getHeader(jwtProperty.getJwtHeader()))) {
            filterChain.doFilter(request, response);
        } else {

            try {
                String accessToken = jwtTokenProvider.resolveAccessToken(request);
                boolean isTokenValid = jwtTokenProvider.validateToken(accessToken);

                if (StringUtils.hasText(accessToken) && isTokenValid) {
                    setAuthentication(accessToken);
                }

                filterChain.doFilter(request, response);

            } catch (ExpiredJwtException e) {
                new ExResult("401", e.getMessage());
            }
        }
    }

    private void setAuthentication(String token) {
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        String[] excludePath = {"/js", "/error", "/token/reissue"};
        return stream(excludePath).anyMatch(path::startsWith);
    }
}
