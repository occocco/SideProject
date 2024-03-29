package com.toy.overall_practice.service.member;

import com.toy.overall_practice.domain.member.MemberDetails;
import com.toy.overall_practice.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public MemberDetails loadUserByUsername(String loginId) {
        return new MemberDetails(memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다. 잘못된 접근입니다.")));
    }
}
