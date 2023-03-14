package com.toy.overall_practice.api.member;

import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.jwt.JwtTokenProvider;
import com.toy.overall_practice.jwt.Token;
import com.toy.overall_practice.service.member.service.MemberService;
import com.toy.overall_practice.service.member.service.dto.MemberDto;
import com.toy.overall_practice.service.member.service.dto.MemberInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody MemberDto memberDto,
                                       HttpServletResponse response) {
        return ResponseEntity.ok().body(memberService.login(memberDto, response));
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveAccessToken(request);
        memberService.logout(token);
    }

    @PostMapping("/members")
    public ResponseEntity<MemberDto> join(@RequestBody MemberDto memberDto) {
        memberService.signup(memberDto);
        return ResponseEntity.ok().body(memberDto);
    }

    @GetMapping("/members")
    public ResponseEntity<MemberDto> getInfo(Principal principal) {
        Member member = memberService.findById(principal.getName()).orElseThrow();
        MemberDto memberDto = MemberDto.toMemberDto(member);
        return ResponseEntity.ok().body(memberDto);
    }

    @PatchMapping("/members")
    public ResponseEntity<MemberDto> modifyInfo(@RequestBody MemberInfoDto memberDto, Principal principal) {
        MemberDto modifyMember = memberService.modifyInfo(memberDto, principal);
        return ResponseEntity.ok().body(modifyMember);
    }

}
