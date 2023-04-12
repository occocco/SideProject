package com.toy.overall_practice.domain.wallet;

import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.domain.role.RoleType;
import com.toy.overall_practice.exception.InsufficientFundsException;
import com.toy.overall_practice.service.wallet.dto.WalletCreateDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WalletTest {

    @Test
    void createWallet() {
        Member member = Member.createMember("walletTestMember", "1234", RoleType.MEMBER);
        WalletCreateDto form = new WalletCreateDto("My Wallet");

        Wallet wallet = Wallet.createWallet(form, member);

        assertThat(member.getWallet()).isEqualTo(wallet);
    }

    @Test
    void addBalanceTest() {

        long amount = 1000L;

        Wallet wallet = getWallet();
        Long balance = wallet.getBalance();

        assertThat(balance).isEqualTo(0L);
        wallet.addBalance(amount);

        assertThat(wallet.getBalance()).isEqualTo(balance + amount);

    }

    @Test
    void removeBalanceTest(){

        long amount = 1000L;
        Wallet wallet = getWallet();
        Long balance = wallet.getBalance() + amount;

        assertThat(balance).isEqualTo(amount);
        assertThatThrownBy(() -> wallet.removeBalance(amount * 2)).isInstanceOf(InsufficientFundsException.class);

    }


    private Wallet getWallet() {
        Member member = Member.createMember("walletTestMember", "1234", RoleType.MEMBER);
        WalletCreateDto form = new WalletCreateDto("My Wallet");
        return Wallet.createWallet(form, member);
    }
}