package com.toy.overall_practice.service.member.service.dto;

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

}
