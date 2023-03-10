package com.toy.overall_practice.api.auth;

import com.toy.overall_practice.domain.role.RoleType;
import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.jwt.JwtProperty;
import com.toy.overall_practice.jwt.JwtTokenProvider;
import com.toy.overall_practice.jwt.Token;
import com.toy.overall_practice.redis.RedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthControllerTest {
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    RedisRepository redisRepository;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    JwtProperty jwtProperty;

    @BeforeEach
    public void init() {
        redisRepository.deleteAll();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    void reissueTokenTest() {
        Token token = createToken();
        saveRedis(token);

        String url = "http://localhost:8080/token/reissue";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token.getValue());

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Token> response = restTemplate.postForEntity(url, requestEntity, Token.class);
        Token responseToken = response.getBody();

        assertEquals(responseToken.getKey(), token.getKey());
        assertEquals(responseToken.getExpiredTime(), jwtProperty.getAccessTokenValidTime());
        assertNotEquals(responseToken.getValue(), token.getValue());
    }

    private Token saveRedis(Token token) {
        return redisRepository.save(token);
    }

    private Token createToken() {
        Member member = Member.createMember("MemberC", "123", RoleType.MEMBER);
        return jwtTokenProvider.createToken(member, 1000L);
    }
}