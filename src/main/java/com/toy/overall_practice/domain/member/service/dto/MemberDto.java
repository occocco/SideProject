package com.toy.overall_practice.domain.member.service.dto;

import com.toy.overall_practice.common.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberDto {

    private String loginId;
    private String password;
    private RoleType roleType;

}
