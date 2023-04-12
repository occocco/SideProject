package com.toy.overall_practice.api.member;

import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.service.member.MemberService;
import com.toy.overall_practice.service.member.dto.MemberDto;
import com.toy.overall_practice.service.member.dto.MemberInfoDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "회원 REST API", tags = {"Member REST API"})
@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberService memberService;

    @ApiOperation(value = "회원 저장", notes = "회원 가입 시 사용되는 API")
    @PostMapping("/members")
    public ResponseEntity<MemberDto> join(@RequestBody MemberDto memberDto) {
        memberService.signup(memberDto);
        return ResponseEntity.ok().body(memberDto);
    }

    @ApiOperation(value = "회원 정보 조회", notes = "회원의 현재 정보 조회")
    @GetMapping("/members/{id}")
    public ResponseEntity<MemberDto> getInfo(@PathVariable String id) {
        Member member = memberService.findById(id).orElseThrow();
        MemberDto memberDto = MemberDto.toMemberDto(member);
        return ResponseEntity.ok().body(memberDto);
    }

    @ApiOperation(value = "회원 정보 수정")
    @PatchMapping("/members/{id}")
    public ResponseEntity<MemberDto> modifyInfo(@RequestBody MemberInfoDto memberDto,
                                                @PathVariable String id) {
        MemberDto modifyMember = memberService.modifyInfo(memberDto, id);
        return ResponseEntity.ok().body(modifyMember);
    }

    @ApiOperation(value = "회원 탈퇴")
    @DeleteMapping("/members/{id}")
    public void WithdrawMember(@PathVariable String id) {
        memberService.delete(id);
    }

}
