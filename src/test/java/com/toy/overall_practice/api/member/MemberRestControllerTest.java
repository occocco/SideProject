package com.toy.overall_practice.api.member;

import com.toy.overall_practice.domain.role.RoleType;
import com.toy.overall_practice.jwt.Token;
import com.toy.overall_practice.service.member.dto.MemberDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class MemberRestControllerTest {

    RestTemplate restTemplate;
    HttpHeaders headers;
    Map<String, String> requestBody;

    @BeforeEach
    void init() {
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        requestBody = new HashMap<>();
    }

    @Test
    void joinTest() {
        String uuid = UUID.randomUUID().toString();
        MemberDto memberDto = new MemberDto(uuid, "123", null);

        String url = "http://localhost:8080/members";

        requestBody.put("loginId", memberDto.getLoginId());
        requestBody.put("password", memberDto.getPassword());

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<MemberDto> response = restTemplate.postForEntity(url, requestEntity, MemberDto.class);
        MemberDto savedMember = response.getBody();

        assertEquals(savedMember, memberDto);
        assertEquals(savedMember.getLoginId(), memberDto.getLoginId());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void joinExTest() {
        String uuid = UUID.randomUUID().toString();
        MemberDto memberDto1 = new MemberDto(uuid, "123", null);
        MemberDto memberDto2 = new MemberDto(uuid, "123", null);

        String url = "http://localhost:8080/members";

        requestBody.put("loginId", memberDto1.getLoginId());
        requestBody.put("password", memberDto1.getPassword());
        HttpEntity<Map<String, String>> requestEntity1 = new HttpEntity<>(requestBody, headers);
        ResponseEntity<MemberDto> response1 = restTemplate.postForEntity(url, requestEntity1, MemberDto.class);

        assertEquals(200, response1.getStatusCodeValue());

        requestBody.put("loginId", memberDto2.getLoginId());
        requestBody.put("password", memberDto2.getPassword());
        HttpEntity<Map<String, String>> requestEntity2 = new HttpEntity<>(requestBody, headers);

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            restTemplate.postForEntity(url, requestEntity2, MemberDto.class);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());

    }

    @Test
    void loginTest() {
        MemberDto memberDto = new MemberDto("MemberC", "123", RoleType.MEMBER);

        String url = "http://localhost:8080/login";
        headers.setContentType(MediaType.APPLICATION_JSON);

        requestBody.put("loginId", memberDto.getLoginId());
        requestBody.put("password", memberDto.getPassword());

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Token> response = restTemplate.postForEntity(url, requestEntity, Token.class);
        Token responseToken = response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(memberDto.getLoginId(), responseToken.getKey());

    }

    @Test
    void loginExTest() {

        MemberDto memberDto = new MemberDto("MemberC", "wrongPass", RoleType.MEMBER);

        String url = "http://localhost:8080/login";

        requestBody.put("loginId", memberDto.getLoginId());
        requestBody.put("password", memberDto.getPassword());

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            restTemplate.postForEntity(url, requestEntity, Token.class);
        });

        assertThat(exception.getMessage()).contains("아이디 혹은 비밀번호가 올바르지 않습니다");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

    }

}