package com.toy.overall_practice.jwt;

import com.toy.overall_practice.domain.role.RoleType;
import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.redis.RedisRepository;
import com.toy.overall_practice.service.member.service.MemberDetailService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.lang.reflect.Field;
import java.security.Key;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    String key = "temporary1secret1key1for1testing1and1development1only";
    @InjectMocks
    JwtTokenProvider jwtTokenProvider;
    @Mock
    JwtProperty jwtProperty;
    @Mock
    RedisRepository redisRepository;
    @Mock
    MemberDetailService memberDetailService;

    @BeforeEach
    void init() throws Exception {

        Field field = JwtTokenProvider.class.getDeclaredField("key");
        field.setAccessible(true);
        byte[] keyBytes = Decoders.BASE64.decode(key);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        field.set(jwtTokenProvider, key);

        Field jwtHeader = JwtTokenProvider.class.getDeclaredField("jwtProperty");
        jwtHeader.setAccessible(true);
        JwtProperty property = new JwtProperty();
        property.setJwtHeader("Authorization");
        property.setJwtTokenPrefix("Bearer");
        jwtHeader.set(jwtTokenProvider, property);

    }

    @Test
    void createTokenTest() {

        Member member = Member.createMember("memberA", "1234", RoleType.MEMBER);

        Token token = jwtTokenProvider.createToken(member, 1000L);

        assertThat(token.getKey()).isEqualTo(member.getLoginId());
        assertThat(token.getExpiredTime()).isEqualTo(1000L);

    }

    @Test
    void getMemberLoginIdTest() {

        Member member = Member.createMember("memberA", "1234", RoleType.MEMBER);
        Token token = jwtTokenProvider.createToken(member, 1000L);

        String memberLoginId = jwtTokenProvider.getMemberLoginId(token.getValue());

        assertThat(memberLoginId).isEqualTo(member.getLoginId());

    }

    @Test
    void resolveAccessTokenTest() {

        Member member = Member.createMember("memberA", "1234", RoleType.MEMBER);
        Token token = jwtTokenProvider.createToken(member, 1000L);

        MockHttpServletRequest mockHttpServletRequest = MockMvcRequestBuilders.get("/")
                .header("Authorization", "Bearer " + token.getValue())
                .buildRequest(new MockServletContext());

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token.getValue());
        String resolveAccessToken = jwtTokenProvider.resolveAccessToken(request);

        assertThat(resolveAccessToken).isNotNull().isEqualTo(token.getValue());

    }

    @Test
    void validateTokenTest() throws InterruptedException {

        Member member = Member.createMember("memberA", "1234", RoleType.MEMBER);
        Token token = jwtTokenProvider.createToken(member, 2000L);


        assertThat(jwtTokenProvider.validateToken(token.getValue())).isEqualTo(true);

        Thread.sleep(2001L);
        assertThatThrownBy(()->jwtTokenProvider.validateToken(token.getValue())).isInstanceOf(ExpiredJwtException.class);

        String tokenEx = token.getValue().split("\\.")[2] + "Exception";
        assertThat(jwtTokenProvider.validateToken(tokenEx)).isEqualTo(false);

    }

}