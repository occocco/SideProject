package com.toy.overall_practice.domain.member.service;

import com.toy.overall_practice.domain.role.RoleType;
import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.exception.DuplicateMemberException;
import com.toy.overall_practice.exception.NotFoundMemberException;
import com.toy.overall_practice.jwt.JwtTokenProvider;
import com.toy.overall_practice.jwt.Token;
import com.toy.overall_practice.redis.RedisRepository;
import com.toy.overall_practice.service.member.service.MemberServiceImpl;
import com.toy.overall_practice.service.member.service.dto.MemberDto;
import com.toy.overall_practice.service.member.service.dto.MemberInfoDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

import java.security.Principal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @Autowired
    MemberServiceImpl memberServiceImpl;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    RedisRepository redisRepository;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Mock
    Principal principal;
    @Mock
    Authentication authentication;

    @BeforeEach
    void init() {
        MemberDto memberDto = new MemberDto("TestMember", "123", RoleType.MEMBER);
        memberServiceImpl.signup(memberDto);
    }

    @AfterEach
    void after() {
        redisRepository.deleteAll();
    }

    @Test
    void joinMemberTest() {
        MemberDto memberDto = new MemberDto("MemberA", "1234", RoleType.MEMBER);
        Member member = memberServiceImpl.signup(memberDto);
        assertThat(member.getLoginId()).isEqualTo("MemberA");
    }

    @Test
    void validateDuplicateMemberTest() {
        MemberDto memberDtoA = new MemberDto("MemberA", "1234", RoleType.MEMBER);
        MemberDto memberDtoB = new MemberDto("MemberA", "1234", RoleType.MEMBER);
        memberServiceImpl.signup(memberDtoA);
        assertThatThrownBy(() -> memberServiceImpl.signup(memberDtoB)).isInstanceOf(DuplicateMemberException.class);
    }

    @Test
    void loginTest() {
        HttpServletResponse response = mock(HttpServletResponse.class);
        MemberDto memberDto = new MemberDto("TestMember", "123", RoleType.MEMBER);

        Token token = memberServiceImpl.login(memberDto, response);

        assertThat(token.getKey()).isEqualTo(memberDto.getLoginId());
    }

    @Test
    void loginExTest() {
        HttpServletResponse response = mock(HttpServletResponse.class);
        MemberDto memberDto = new MemberDto("TestMember", "123", RoleType.MEMBER);

        memberDto.setPassword("wrongPass");

        assertThatThrownBy(() -> memberServiceImpl.login(memberDto, response)).isInstanceOf(NotFoundMemberException.class);
    }

    @Test
    void modifyInfoTest(){
        when(principal.getName()).thenReturn("TestMember");
        when(authentication.getPrincipal()).thenReturn(principal);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MemberInfoDto memberInfoDto = new MemberInfoDto("modify");

        MemberDto modifyInfo = memberServiceImpl.modifyInfo(memberInfoDto, "TestMember");

        Member member = memberServiceImpl.findById(principal.getName()).orElseThrow();
        Assertions.assertEquals(modifyInfo.getPassword(), member.getPassword());

        SecurityContextHolder.clearContext();
    }

    @Test
    void logoutTest(){
        Token token = createToken();
        Token savedToken = redisRepository.save(token);

        memberServiceImpl.logout(savedToken.getValue());

        Optional<Token> refreshToken = redisRepository.findById(savedToken.getKey());
        assertThat(refreshToken).isEmpty();
    }
    private Token createToken() {
        Member member = Member.createMember("TestMember", "123", RoleType.MEMBER);
        return jwtTokenProvider.createToken(member, 1000L);
    }
}