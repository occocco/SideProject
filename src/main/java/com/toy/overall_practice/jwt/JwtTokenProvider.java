package com.toy.overall_practice.jwt;

import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.domain.member.service.MemberDetailService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private Key key;
    private final JwtProperty jwtProperty;
    private final MemberDetailService memberDetailService;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperty.getSecretKey());
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtToken createAccessToken(Member member) {
        return createToken(member, jwtProperty.getAccessTokenValidTime());
    }

    public JwtToken createRefreshToken(Member member) {
        return createToken(member, jwtProperty.getRefreshTokenValidTime());
    }

    public JwtToken createToken(Member member, Long tokenValidTime) {
        Claims claims = Jwts.claims().setSubject(member.getLoginId());
        claims.put("role", member.getRole().stream().findAny().get().getRole().getRoleName().name());
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + tokenValidTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .setHeaderParam("typ","JWT")
                .compact();
        return new JwtToken(member.getLoginId(), token, tokenValidTime);
    }

    // JWT 토큰에서 인증 정보 조회
    @Transactional(readOnly = true)
    public Authentication getAuthentication(String token) {
        HashMap<String, String> payloadMap = JwtUtil.getPayloadInToken(token);
        UserDetails userDetails = memberDetailService.loadUserByUsername(payloadMap.get("sub"));
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    // 토큰으로 회원 정보 조회
    public String getMemberLoginId(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    // GET ACCESS TOKEN BY COOKIE
    public String resolveAccessToken(HttpServletRequest request) {
        Optional<Cookie> findCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(jwtProperty.getJwtHeader()))
                .findAny();
        if (findCookie.isPresent()) {
            String bearerToken = findCookie.get().getValue();
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtProperty.getJwtTokenPrefix())) {
                return bearerToken.substring(6);
            }
        }
        return null;
    }

    // 토큰의 유효 및 만료 확인
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature");
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
            return false;
        } catch (IllegalArgumentException e) {
            log.error("JWT token is invalid");
            return false;
        }
    }

    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader(jwtProperty.getJwtHeader(), accessToken);
    }
}
