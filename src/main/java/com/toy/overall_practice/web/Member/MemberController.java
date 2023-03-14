package com.toy.overall_practice.web.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "redirect", required = false, defaultValue = "/") String redirectUri, Model model) {
        model.addAttribute("redirectUri", redirectUri);
        return "member/LoginForm";
    }

    @GetMapping("/join")
    public String joinForm() {
        return "member/JoinForm";
    }

}
