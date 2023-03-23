package com.toy.overall_practice.service.transaction;

import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.domain.transaction.Transaction;
import com.toy.overall_practice.domain.transaction.TransactionRepository;
import com.toy.overall_practice.exception.NotFoundMemberException;
import com.toy.overall_practice.service.member.MemberService;
import com.toy.overall_practice.service.transaction.dto.TransactionDto;
import com.toy.overall_practice.service.transaction.dto.TransactionLogDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;
    private final MemberService memberService;

    @Transactional
    public Transaction createTransaction(TransactionDto transactionDto) {
        Member sender = getMember(memberService.findById(transactionDto.getSender()));
        Member receiver = getMember(memberService.findById(transactionDto.getReceiver()));
        Transaction tx = Transaction.createTx(sender, receiver, transactionDto.getAmount());
        return repository.save(tx);
    }

    public List<TransactionLogDto> findTxRecord(String loginId) {
        Member member = getMember(memberService.findById(loginId));
        List<Transaction> txs = repository.findAllByMemberAndPeriod(
                member,
                LocalDateTime.now().minusDays(1L),
                LocalDateTime.now().plusDays(1L));
        Long balance = 0L;
        List<TransactionLogDto> txLogList = new ArrayList<>();
        for (Transaction tx : txs) {
            if (tx.getSender().equals(member)) {
                balance += tx.getReceiver().equals(member) ? tx.getAmount() : Long.valueOf(-tx.getAmount());
            } else {
                balance += tx.getAmount();
            }
            txLogList.add(TransactionLogDto.toTransactionDto(tx, balance));
        }
        return txLogList;
    }

    private Member getMember(Optional<Member> member) {
        if (member.isEmpty()) {
            throw new NotFoundMemberException("회원을 찾을 수 없습니다.");
        } else {
            return member.get();
        }
    }
}
