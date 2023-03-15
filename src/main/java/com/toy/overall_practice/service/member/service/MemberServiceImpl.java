package com.toy.overall_practice.service.member.service;

import com.toy.overall_practice.domain.role.RoleType;
import com.toy.overall_practice.exception.DuplicateMemberException;
import com.toy.overall_practice.jwt.Token;
import com.toy.overall_practice.jwt.JwtTokenProvider;
import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.domain.member.repository.MemberRepository;
import com.toy.overall_practice.redis.RedisRepository;
import com.toy.overall_practice.service.member.service.dto.MemberDto;
import com.toy.overall_practice.exception.NotFoundMemberException;
import com.toy.overall_practice.service.member.service.dto.MemberInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final RedisRepository redisRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public Member signup(MemberDto memberDto) {
        validateDuplicateMember(memberDto.getLoginId());
        String password = passwordEncoder.encode(memberDto.getPassword());
        Member member = Member.createMember(memberDto.getLoginId(), password, RoleType.MEMBER);
        return memberRepository.save(member);
    }

    @Override
    public Token login(MemberDto memberDto, HttpServletResponse response) {
        Optional<Member> findMember = memberRepository.findByLoginId(memberDto.getLoginId());
        if (findMember.isEmpty() || !passwordEncoder.matches(memberDto.getPassword(), findMember.get().getPassword())) {
            throw new NotFoundMemberException("아이디 혹은 비밀번호가 올바르지 않습니다");
        }
        Token accessToken = jwtTokenProvider.createAccessToken(findMember.get());
        Token refreshToken = jwtTokenProvider.createRefreshToken(findMember.get());

        jwtTokenProvider.setHeaderAccessToken(response, accessToken.getValue());

        redisRepository.save(refreshToken);

        return accessToken;
    }

    @Override
    public void logout(String token) {
        String loginId = jwtTokenProvider.getMemberLoginId(token);
        redisRepository.deleteById(loginId);
    }

    @Override
    public Optional<Member> findById(String id) {
        return memberRepository.findByLoginId(id);
    }

    @Override
    @Transactional
    public MemberDto modifyInfo(MemberInfoDto memberDto, String id) {
        Member member = memberRepository.findByLoginId(id)
                .orElseThrow(()-> new NotFoundMemberException("회원정보가 올바르지 않습니다."));
        String password = passwordEncoder.encode(memberDto.getPassword());
        member.modifyMember(password);
        return MemberDto.toMemberDto(member);
    }

    @Override
    @Transactional
    public void delete(String id) {
        Member member = memberRepository.findByLoginId(id)
                .orElseThrow(() -> new NotFoundMemberException("회원을 찾을 수 없습니다."));
        memberRepository.deleteById(member.getId());
    }


    private void validateDuplicateMember(String loginId) {
        Optional<Member> findMember = memberRepository.findByLoginId(loginId);
        if (findMember.isPresent()) {
            throw new DuplicateMemberException("이미 존재하는 아이디입니다.");
        }
    }

}
