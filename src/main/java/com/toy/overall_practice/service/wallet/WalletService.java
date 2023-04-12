package com.toy.overall_practice.service.wallet;

import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.domain.member.repository.MemberRepository;
import com.toy.overall_practice.domain.wallet.Wallet;
import com.toy.overall_practice.domain.wallet.WalletRepository;
import com.toy.overall_practice.service.wallet.dto.WalletCreateDto;
import com.toy.overall_practice.service.wallet.dto.WalletUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static com.toy.overall_practice.utils.ConversionUtils.getEntityFromOpt;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final MemberRepository memberRepository;

    public Wallet findWalletByLoginId(String id) {
        Member member = getEntityFromOpt(memberRepository.findWalletByLoginId(id), "회원을 찾을 수 없습니다.");
        try {
            Long walletId = member.getWallet().getId();
            return walletRepository.findById(walletId).orElseThrow();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("지갑이 없습니다.");
        }
    }

    @Transactional
    public Wallet createWallet(WalletCreateDto dto, Member member) {
        validateDuplicateWallet(member.getLoginId());
        Wallet wallet = Wallet.createWallet(dto, member);
        return walletRepository.save(wallet);
    }
    @Transactional
    public Wallet updateWallet(String id, WalletUpdateDto dto) {
        Wallet wallet = findWalletByLoginId(id);
        return wallet.updateWallet(dto);
    }
    @Transactional
    public void removeWallet(String id) {
        Member member = getEntityFromOpt(memberRepository.findWalletByLoginId(id), "회원을 찾을 수 없습니다.");
        Wallet wallet = member.getWallet();
        member.removeWallet();
        walletRepository.deleteById(wallet.getId());
    }

    private void validateDuplicateWallet(String id) {
        Wallet wallet = memberRepository.findByLoginId(id).orElseThrow().getWallet();
        if (wallet != null) {
            throw new DuplicateKeyException("이미 지갑이 존재합니다.");
        }
    }
}
