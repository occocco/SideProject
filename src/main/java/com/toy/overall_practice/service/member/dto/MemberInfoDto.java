package com.toy.overall_practice.service.member.dto;

import com.toy.overall_practice.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoDto {

    private String password;

    public static MemberInfoDto toMemberInfoDto(Member member) {
        return new MemberInfoDto(member.getPassword());
    }
}
