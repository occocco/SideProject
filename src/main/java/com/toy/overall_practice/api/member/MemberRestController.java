package com.toy.overall_practice.api.member;

import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.jwt.JwtTokenProvider;
import com.toy.overall_practice.jwt.Token;
import com.toy.overall_practice.service.member.MemberService;
import com.toy.overall_practice.service.member.dto.MemberDto;
import com.toy.overall_practice.service.member.dto.MemberInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @GetMapping("/members/{id}")
    public ResponseEntity<MemberDto> getInfo(@PathVariable String id) {
        Member member = memberService.findById(id).orElseThrow();
        MemberDto memberDto = MemberDto.toMemberDto(member);
        return ResponseEntity.ok().body(memberDto);
    }

    @PatchMapping("/members/{id}")
    public ResponseEntity<MemberDto> modifyInfo(@RequestBody MemberInfoDto memberDto,
                                                @PathVariable String id) {
        MemberDto modifyMember = memberService.modifyInfo(memberDto, id);
        return ResponseEntity.ok().body(modifyMember);
    }

    @DeleteMapping("/members/{id}")
    public void WithdrawMember(@PathVariable String id) {
        memberService.delete(id);
    }

}
