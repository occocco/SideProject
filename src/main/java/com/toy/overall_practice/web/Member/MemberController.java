package com.toy.overall_practice.web.Member;

import com.toy.overall_practice.domain.member.service.MemberService;
import com.toy.overall_practice.domain.member.service.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String loginForm(@RequestParam("redirect") String redirectUri, Model model) {
        model.addAttribute("redirectUri", redirectUri);
        return "LoginForm";
    }

    @PostMapping("/login")
    public String login(@RequestParam("redirect") String redirectUri,
                        @ModelAttribute MemberDto memberDto,
                        HttpServletResponse response) {
        memberService.login(memberDto, response);
        return "redirect:" + redirectUri;
    }

    @GetMapping("/join")
    public String joinForm() {
        return "JoinForm";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute MemberDto memberDto) {
        memberService.join(memberDto);
        return "redirect:/login?redirect=/";
    }
}
