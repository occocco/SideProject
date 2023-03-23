package com.toy.overall_practice.service.member.dto;

import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.domain.role.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private String loginId;
    private String password;
    private RoleType roleType;

    public static MemberDto toMemberDto(Member member) {
        return new MemberDto(member.getLoginId(),
                             member.getPassword(),
                             member.getRole().stream().findAny().orElseThrow().getRole().getRoleName());
    }
}
