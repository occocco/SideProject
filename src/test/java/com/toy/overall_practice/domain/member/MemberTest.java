package com.toy.overall_practice.domain.member;

import com.toy.overall_practice.domain.role.RoleType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MemberTest {

    @Test
    void createMemberTest() {

        Member memberA = getMember("memberA");
        Member member = memberA.getRole().stream().findAny().orElseThrow().getMember();

        assertThat(member).isEqualTo(memberA);
        assertThat(memberA.getLoginId()).isEqualTo("memberA");

    }

    @Test
    void modifyTest(){
        Member memberA = getMember("MemberA");
        String modifyPassword = "0000";

        memberA.modifyMember(modifyPassword);

        assertThat(memberA.getPassword()).isEqualTo(modifyPassword);
    }

    private Member getMember(String memberId) {
        return Member.createMember(memberId, "1234", RoleType.MEMBER);
    }
}