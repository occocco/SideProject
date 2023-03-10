package com.toy.overall_practice.api.auth;

import com.toy.overall_practice.jwt.JwtTokenProvider;
import com.toy.overall_practice.jwt.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/token/reissue")
    public ResponseEntity<Token> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok().body(jwtTokenProvider.reissueToken(request, response));
    }
}
