package com.toy.overall_practice.domain.transaction;

import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.domain.role.RoleType;
import com.toy.overall_practice.domain.wallet.Wallet;
import com.toy.overall_practice.exception.InsufficientFundsException;
import com.toy.overall_practice.service.wallet.dto.WalletCreateForm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void createTxTest() {
        long amount = 1000L;
        Member sender = Member.createMember("sender", "1234", RoleType.MEMBER);
        Wallet senderWallet = getWallet(sender);
        senderWallet.addBalance(amount);
        Long senderBalance = senderWallet.getBalance();

        Member receiver = Member.createMember("receiver", "1234", RoleType.MEMBER);
        Wallet receiverWallet = getWallet(receiver);
        Long receiverBalance = receiverWallet.getBalance();

        assertEquals(senderWallet.getBalance(), amount);
        assertEquals(receiverWallet.getBalance(), 0L);

        Transaction.createTx(sender, receiver, amount);

        assertEquals(senderWallet.getBalance(), senderBalance - amount);
        assertEquals(receiverWallet.getBalance(), receiverBalance + amount);

    }

    @Test
    void insufficientFundsExTest() {
        long amount = 1000L;
        long excessAmount = 2000L;
        Member sender = Member.createMember("sender", "1234", RoleType.MEMBER);
        Wallet senderWallet = getWallet(sender);
        senderWallet.addBalance(amount);
        Long senderBalance = senderWallet.getBalance();

        Member receiver = Member.createMember("receiver", "1234", RoleType.MEMBER);
        Wallet receiverWallet = getWallet(receiver);
        Long receiverBalance = receiverWallet.getBalance();


        assertThrows(InsufficientFundsException.class, () -> Transaction.createTx(sender, receiver, excessAmount));

        assertEquals(senderBalance, amount);
        assertEquals(receiverBalance, 0L);

    }

    @Test
    void rechargeWallet() {

        Member sameMember = Member.createMember("sameMember", "123", RoleType.MEMBER);
        Wallet sameMemberWallet = getWallet(sameMember);
        long amount = 1000L;
        Long beforeTxBalance = sameMemberWallet.getBalance();
        assertEquals(beforeTxBalance, 0L);

        Transaction.createTx(sameMember, sameMember, amount);

        assertEquals(sameMemberWallet.getBalance(), amount);

    }

    private Wallet getWallet(Member member) {
        WalletCreateForm form = new WalletCreateForm("My Wallet", member);
        return Wallet.createWallet(form);
    }

}