package com.toy.overall_practice.service.member.service;

import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.jwt.Token;
import com.toy.overall_practice.service.member.service.dto.MemberDto;
import com.toy.overall_practice.service.member.service.dto.MemberInfoDto;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Optional;

public interface MemberService {

    Member signup(MemberDto memberDto);

    Token login(MemberDto memberDto, HttpServletResponse response);

    void logout(String token);
    Optional<Member> findById(String id);

    MemberDto modifyInfo(MemberInfoDto memberDto, Principal principal);
}
