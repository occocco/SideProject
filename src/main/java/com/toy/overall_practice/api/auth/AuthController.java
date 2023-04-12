package com.toy.overall_practice.api.auth;

import com.toy.overall_practice.jwt.JwtTokenProvider;
import com.toy.overall_practice.jwt.Token;
import com.toy.overall_practice.service.member.MemberService;
import com.toy.overall_practice.service.member.dto.MemberDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Api(value = "Auth REST API", tags = {"Auth REST API"})
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

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

    @ApiOperation(value = "토큰 재발급 API", notes = "refresh 토큰을 통해 새로운 토큰을 발급 받는 API")
    @PostMapping("/token/reissue")
    public ResponseEntity<Token> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok().body(jwtTokenProvider.reissueToken(request, response));
    }
}
