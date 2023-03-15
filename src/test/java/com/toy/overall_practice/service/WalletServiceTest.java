package com.toy.overall_practice.service;

import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.domain.wallet.Wallet;
import com.toy.overall_practice.service.member.service.MemberService;
import com.toy.overall_practice.service.wallet.WalletService;
import com.toy.overall_practice.service.wallet.dto.WalletCreateForm;
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
        Member member = getMember("123");
        WalletCreateForm form = new WalletCreateForm("My Wallet", member);
        Wallet wallet = walletService.createWallet(form);

        assertThat(member.getWallet()).isEqualTo(wallet);
        assertThat(wallet.getMember().getLoginId()).isEqualTo(member.getLoginId());
    }

    @Test
    void getWalletTest() {

        Member member = getMember("MemberC");
        WalletCreateForm form = new WalletCreateForm("My Wallet", member);
        walletService.createWallet(form);

        Wallet findWallet = walletService.findWalletByLoginId(member.getLoginId());

        assertThat(findWallet.getMember()).isEqualTo(member);
        assertThat(findWallet.getBalance()).isEqualTo(0L);
        assertThat(findWallet.getId()).isEqualTo(member.getWallet().getId());
    }

    @Test
    void validateDuplicateWalletTest() {

        Member member = getMember("123");
        WalletCreateForm form = new WalletCreateForm("My Wallet", member);
        walletService.createWallet(form);

        assertThrows(DuplicateKeyException.class, () -> walletService.createWallet(form));
    }

    private Member getMember(String id) {
        return memberService.findById(id).get();
    }
}