package com.toy.overall_practice.domain.member.service;

import com.toy.overall_practice.common.RoleType;
import com.toy.overall_practice.jwt.JwtProperty;
import com.toy.overall_practice.jwt.JwtToken;
import com.toy.overall_practice.jwt.JwtTokenProvider;
import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.domain.member.repository.MemberRepository;
import com.toy.overall_practice.domain.member.service.dto.MemberDto;
import com.toy.overall_practice.exception.DuplicateMemberException;
import com.toy.overall_practice.exception.NotFoundMemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final JwtProperty jwtProperty;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Member join(MemberDto memberDto) {
        validateDuplicateMember(memberDto.getLoginId());
        String password = passwordEncoder.encode(memberDto.getPassword());
        Member member = Member.createMember(memberDto.getLoginId(), password, RoleType.MEMBER);
        return memberRepository.save(member);
    }

    public void login(MemberDto memberDto, HttpServletResponse response) {
        Optional<Member> findMember = memberRepository.findByLoginId(memberDto.getLoginId());
        if (findMember.isEmpty() || !passwordEncoder.matches(memberDto.getPassword(), findMember.get().getPassword())) {
            throw new NotFoundMemberException("아이디 혹은 비밀번호가 올바르지 않습니다");
        }
        JwtToken accessToken = jwtTokenProvider.createAccessToken(findMember.get());
        Cookie cookie = new Cookie(jwtProperty.getJwtHeader(), "Bearer" + accessToken.getValue());
        cookie.setPath("/");
        cookie.setMaxAge(Math.toIntExact(jwtProperty.getAccessTokenValidTime()));
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    private void validateDuplicateMember(String loginId) {
        Optional<Member> findMember = memberRepository.findByLoginId(loginId);
        if (findMember.isPresent()) {
            throw new DuplicateMemberException("이미 존재하는 아이디입니다.");
        }
    }

}
