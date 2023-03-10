package com.toy.overall_practice.service.member.service;

import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.jwt.Token;
import com.toy.overall_practice.service.member.service.dto.MemberDto;

import javax.servlet.http.HttpServletResponse;

public interface MemberService {

    Member signup(MemberDto memberDto);

    Token login(MemberDto memberDto, HttpServletResponse response);

}
