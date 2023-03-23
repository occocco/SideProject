package com.toy.overall_practice.jwt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Data
@Component
public class JwtProperty {
    @Value("${jwt.security.key}")
    private String secretKey;
    @Value("${jwt.response.header}")
    private String jwtHeader;
    @Value("${jwt.token.prefix}")
    private String jwtTokenPrefix;

//    private Long accessTokenValidTime = Duration.ofMinutes(30).toMillis();
    private Long accessTokenValidTime = Duration.ofMinutes(300).toMillis();
    private Long refreshTokenValidTime = Duration.ofDays(7).toMillis();

}
