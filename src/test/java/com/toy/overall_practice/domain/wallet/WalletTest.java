package com.toy.overall_practice.domain.wallet;

import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.domain.role.RoleType;
import com.toy.overall_practice.service.wallet.dto.WalletCreateForm;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    @Test
    void createWallet(){
        Member member = Member.createMember("walletTestMember", "1234", RoleType.MEMBER);
        WalletCreateForm form = new WalletCreateForm("My Wallet", member);

        Wallet wallet = Wallet.createWallet(form);

        assertThat(member.getWallet()).isEqualTo(wallet);
        assertThat(member.getLoginId()).isEqualTo(wallet.getMember().getLoginId());
    }

}