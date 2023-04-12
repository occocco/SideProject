package com.toy.overall_practice.api.wallet;

import com.toy.overall_practice.api.ResponseResult;
import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.domain.wallet.Wallet;
import com.toy.overall_practice.service.wallet.WalletService;
import com.toy.overall_practice.service.member.MemberService;
import com.toy.overall_practice.service.wallet.dto.WalletCreateDto;
import com.toy.overall_practice.service.wallet.dto.WalletDto;
import com.toy.overall_practice.service.wallet.dto.WalletUpdateDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.toy.overall_practice.utils.ConversionUtils.getEntityFromOpt;


@Api(value = "지갑 REST API", tags = {"Wallet REST API"})
@RestController
@RequiredArgsConstructor
public class WalletRestController {

    private final WalletService walletService;
    private final MemberService memberService;

    @ApiOperation(value = "지갑 조회")
    @GetMapping("/wallets/{id}")
    public WalletDto getWallet(@PathVariable String id) {
        Wallet wallet = walletService.findWalletByLoginId(id);
        return WalletDto.toWalletDto(wallet);
    }

    @ApiOperation(value = "지갑 생성", notes = "회원의 지갑 리소스를 생성하는 API")
    @PostMapping("/wallets")
    public ResponseEntity<WalletDto> createWallet(@RequestBody WalletCreateDto walletCreateDto, Principal principal) {
        Member member = getEntityFromOpt(memberService.findById(principal.getName()), "회원정보가 올바르지 않습니다.");
        Wallet wallet = walletService.createWallet(walletCreateDto, member);
        return ResponseEntity.ok().body(WalletDto.toWalletDto(wallet));
    }

    @ApiOperation(value = "지갑 수정", notes = "회원의 지갑 리소스를 수정하는 API")
    @PatchMapping("/wallets/{id}")
    public ResponseEntity<WalletDto> updateWallet(@PathVariable String id,
                                                  @RequestBody WalletUpdateDto dto) {
        Wallet wallet = walletService.updateWallet(id, dto);
        return ResponseEntity.ok().body(WalletDto.toWalletDto(wallet));
    }

    @ApiOperation(value = "지갑 삭제", notes = "회원의 지갑 리소스를 삭제하는 API")
    @DeleteMapping("/wallets/{id}")
    public ResponseEntity<ResponseResult> removeWallet(@PathVariable String id) {
        walletService.removeWallet(id);
        ResponseResult responseResult = new ResponseResult(HttpStatus.OK.value(), "지갑이 삭제되었습니다.");
        return ResponseEntity.ok().body(responseResult);
    }
}
