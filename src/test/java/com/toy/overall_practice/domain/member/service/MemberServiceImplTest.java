package com.toy.overall_practice.domain.member.service;

import com.toy.overall_practice.domain.role.RoleType;
import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.exception.NotFoundMemberException;
import com.toy.overall_practice.jwt.Token;
import com.toy.overall_practice.service.member.service.MemberServiceImpl;
import com.toy.overall_practice.service.member.service.dto.MemberDto;
import com.toy.overall_practice.exception.DuplicateMemberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @Autowired
    MemberServiceImpl memberServiceImpl;

    @BeforeEach
    void init() {
        MemberDto memberDto = new MemberDto("TestMember", "123", RoleType.MEMBER);
        memberServiceImpl.signup(memberDto);
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
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        MemberDto memberDto = new MemberDto("TestMember", "123", RoleType.MEMBER);

        Token token = memberServiceImpl.login(memberDto, response);

        assertThat(token.getKey()).isEqualTo(memberDto.getLoginId());
    }

    @Test
    void loginExTest() {
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        MemberDto memberDto = new MemberDto("TestMember", "123", RoleType.MEMBER);

        memberDto.setPassword("wrongPass");

        assertThatThrownBy(() -> memberServiceImpl.login(memberDto, response)).isInstanceOf(NotFoundMemberException.class);
    }
}