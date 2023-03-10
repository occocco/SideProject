package com.toy.overall_practice.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.domain.member.MemberDetails;
import com.toy.overall_practice.exception.ForbiddenException;
import com.toy.overall_practice.redis.RedisRepository;
import com.toy.overall_practice.service.member.service.MemberDetailService;
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
    private final RedisRepository redisRepository;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperty.getSecretKey());
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Token createAccessToken(Member member) {
        return createToken(member, jwtProperty.getAccessTokenValidTime());
    }

    public Token createRefreshToken(Member member) {
        return createToken(member, jwtProperty.getRefreshTokenValidTime());
    }

    public Token createToken(Member member, Long tokenValidTime) {
        Claims claims = Jwts.claims().setSubject(member.getLoginId());
        claims.put("role", member.getRole().stream().findAny().get().getRole().getRoleName().name());
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + tokenValidTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .setHeaderParam("typ", "JWT")
                .compact();
        return new Token(member.getLoginId(), token, tokenValidTime);
    }

    @Transactional(readOnly = true)
    public Authentication getAuthentication(String token) {
        HashMap<String, String> payloadMap = getPayloadInToken(token);
        UserDetails userDetails = memberDetailService.loadUserByUsername(payloadMap.get("sub"));
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    public String getMemberLoginId(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveAccessToken(HttpServletRequest request) {
        String token = request.getHeader(jwtProperty.getJwtHeader());
        if (StringUtils.hasText(token) && token.startsWith(jwtProperty.getJwtTokenPrefix())) {
            return token.substring(7);
        }
        return null;
    }

    public Token reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String token = resolveAccessToken(request);
        HashMap<String, String> payload = getPayloadInToken(token);
        Optional<Token> refreshToken = redisRepository.findById(payload.get("sub"));
        if (refreshToken.isEmpty()) {
            throw new ForbiddenException("인증 정보가 만료되었습니다. 다시 로그인 해주세요.");
        } else {
            MemberDetails userDetails = memberDetailService.loadUserByUsername(payload.get("sub"));
            Token accessToken = createAccessToken(userDetails.getMember());
            setHeaderAccessToken(response,accessToken.getValue());
            return accessToken;
        }
    }

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

    public static HashMap<String, String> getPayloadInToken(String token) {
        try {
            String[] splitJwt = token.split("\\.");
            String payload = new String(Base64.getDecoder().decode(splitJwt[1].getBytes()));
            return new ObjectMapper().readValue(payload, HashMap.class);
        } catch (JsonProcessingException e) {
            log.error("errors ={}", e.getMessage(), e);
            return null;
        }
    }

    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader(jwtProperty.getJwtHeader(), accessToken);
    }
}
