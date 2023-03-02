package com.toy.overall_practice.domain.member.service;

import com.toy.overall_practice.common.RoleType;
import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.domain.member.service.dto.MemberDto;
import com.toy.overall_practice.exception.DuplicateMemberException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;

    @Test
    void createMember(){
        MemberDto memberDto = new MemberDto("MemberA", "1234", RoleType.MEMBER);
        Member member = memberService.join(memberDto);
        assertThat(member.getLoginId()).isEqualTo("MemberA");
    }

    @Test
    void validateDuplicateMember(){
        MemberDto memberDtoA = new MemberDto("MemberA", "1234", RoleType.MEMBER);
        MemberDto memberDtoB = new MemberDto("MemberA", "1234", RoleType.MEMBER);
        memberService.join(memberDtoA);
        assertThatThrownBy(() -> memberService.join(memberDtoB)).isInstanceOf(DuplicateMemberException.class);
    }
}