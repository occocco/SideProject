package com.toy.overall_practice.domain.member;

import com.toy.overall_practice.domain.role.RoleType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MemberTest {

    @Test
    void createMemberTest() {

        Member memberA = Member.createMember("memberA", "1234", RoleType.MEMBER);
        Member member = memberA.getRole().stream().findAny().orElseThrow().getMember();

        assertThat(member).isEqualTo(memberA);
        assertThat(memberA.getLoginId()).isEqualTo("memberA");

    }

    @Test
    void modifyTest(){
        Member memberA = Member.createMember("MemberA", "1234", RoleType.MEMBER);
        String modify = "0000";

        memberA.modifyMember(modify);

        assertThat(memberA.getPassword()).isEqualTo(modify);
    }
}