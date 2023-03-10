package com.toy.overall_practice.api.member;

import com.toy.overall_practice.domain.role.RoleType;
import com.toy.overall_practice.jwt.Token;
import com.toy.overall_practice.service.member.service.dto.MemberDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class MemberRestControllerTest {

    RestTemplate restTemplate;
    HttpHeaders headers;

    @BeforeEach
    void init() {
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
    }

    @Test
    void loginApiTest() {
        MemberDto memberDto = new MemberDto("MemberC", "123", RoleType.MEMBER);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = "http://localhost:8080/login";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("loginId", memberDto.getLoginId());
        requestBody.put("password", memberDto.getPassword());

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Token> response = restTemplate.postForEntity(url, requestEntity, Token.class);
        Token responseToken = response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(memberDto.getLoginId(), responseToken.getKey());

    }

}