package com.toy.overall_practice.api.member;

import com.toy.overall_practice.domain.role.RoleType;
import com.toy.overall_practice.jwt.Token;
import com.toy.overall_practice.service.member.service.dto.MemberDto;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

        String url = "http://localhost:8080/login";
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("loginId", memberDto.getLoginId());
        requestBody.put("password", memberDto.getPassword());

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Token> response = restTemplate.postForEntity(url, requestEntity, Token.class);
        Token responseToken = response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(memberDto.getLoginId(), responseToken.getKey());

    }

    @Test
    void loginApiExTest() {

        MemberDto memberDto = new MemberDto("MemberC", "wrongPass", RoleType.MEMBER);

        String url = "http://localhost:8080/login";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("loginId", memberDto.getLoginId());
        requestBody.put("password", memberDto.getPassword());

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        AbstractThrowableAssert<?, ? extends Throwable> exCase =
                        assertThatThrownBy(() -> restTemplate.postForEntity(url, requestEntity, Token.class))
                        .isInstanceOf(Exception.class);

        exCase.hasMessageContaining("아이디 혹은 비밀번호가 올바르지 않습니다");

    }

}