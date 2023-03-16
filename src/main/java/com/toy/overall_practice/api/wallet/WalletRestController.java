package com.toy.overall_practice.api.wallet;

import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.domain.wallet.Wallet;
import com.toy.overall_practice.exception.NotFoundMemberException;
import com.toy.overall_practice.service.wallet.WalletService;
import com.toy.overall_practice.service.member.service.MemberService;
import com.toy.overall_practice.service.wallet.dto.WalletCreateForm;
import com.toy.overall_practice.service.wallet.dto.WalletDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
public class WalletRestController {

    private final WalletService walletService;
    private final MemberService memberService;

    @GetMapping("/wallets/{id}")
    public WalletDto getWallet(@PathVariable String id) {
        Wallet wallet = walletService.findWalletByLoginId(id);
        return WalletDto.toWalletDto(wallet);
    }
    @PostMapping("/wallets/{id}")
    public ResponseEntity<WalletDto> createWallet(@PathVariable String id,
                                                  @RequestBody WalletCreateForm form) {
        Member member = memberService.findById(id)
                .orElseThrow(()-> new NotFoundMemberException("회원정보가 올바르지 않습니다."));
        form.setMember(member);
        Wallet wallet = walletService.createWallet(form);
        return ResponseEntity.ok().body(WalletDto.toWalletDto(wallet));
    }

}
