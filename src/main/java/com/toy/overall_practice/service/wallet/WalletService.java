package com.toy.overall_practice.service.wallet;

import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.domain.member.repository.MemberRepository;
import com.toy.overall_practice.domain.wallet.Wallet;
import com.toy.overall_practice.domain.wallet.WalletRepository;
import com.toy.overall_practice.exception.NotFoundMemberException;
import com.toy.overall_practice.service.wallet.dto.WalletCreateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Wallet createWallet(WalletCreateForm form) {
        validateDuplicateWallet(form.getMember().getLoginId());
        Wallet wallet = Wallet.createWallet(form);
        return walletRepository.save(wallet);
    }

    public Wallet findWalletByLoginId(String id) {
        Member member = memberRepository.findWalletByLoginId(id)
                .orElseThrow(() -> new NotFoundMemberException("회원을 찾을 수 없습니다."));
        try {
            Long walletId = member.getWallet().getId();
            return walletRepository.findById(walletId).orElseThrow();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("지갑이 없습니다.");
        }
    }

    private void validateDuplicateWallet(String id) {
        Wallet wallet = memberRepository.findByLoginId(id).orElseThrow().getWallet();
        if (wallet != null) {
            throw new DuplicateKeyException("이미 지갑이 존재합니다.");
        }
    }
}
