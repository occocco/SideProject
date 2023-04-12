package com.toy.overall_practice.service;

import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.domain.wallet.Wallet;
import com.toy.overall_practice.service.member.MemberService;
import com.toy.overall_practice.service.wallet.WalletService;
import com.toy.overall_practice.service.wallet.dto.WalletCreateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class WalletServiceTest {

    @Autowired
    WalletService walletService;
    @Autowired
    MemberService memberService;

    @Test
    void createWalletTest() {
        Member member = getMember("1234");
        WalletCreateDto form = new WalletCreateDto("My Wallet");
        Wallet wallet = walletService.createWallet(form, member);

        assertThat(member.getWallet()).isEqualTo(wallet);
    }

    @Test
    void getWalletTest() {

        Member member = getMember("MemberE");
        WalletCreateDto form = new WalletCreateDto("My Wallet");
        walletService.createWallet(form, member);

        Wallet findWallet = walletService.findWalletByLoginId(member.getLoginId());

        assertThat(findWallet.getBalance()).isEqualTo(0L);
        assertThat(findWallet.getId()).isEqualTo(member.getWallet().getId());
    }

    @Test
    void validateDuplicateWalletTest() {

        Member member = getMember("1234");
        WalletCreateDto form = new WalletCreateDto("My Wallet");
        walletService.createWallet(form, member);

        assertThrows(DuplicateKeyException.class, () -> walletService.createWallet(form, member));
    }

    private Member getMember(String id) {
        return memberService.findById(id).get();
    }
}