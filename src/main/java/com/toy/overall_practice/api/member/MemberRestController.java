package com.toy.overall_practice.api.member;

import com.toy.overall_practice.jwt.Token;
import com.toy.overall_practice.service.member.service.MemberService;
import com.toy.overall_practice.service.member.service.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody MemberDto memberDto,
                                       HttpServletResponse response) {
        return ResponseEntity.ok().body(memberService.login(memberDto, response));
    }

}
