package com.toy.overall_practice.domain.transaction;

import com.toy.overall_practice.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE (t.sender = :member OR t.receiver = :member) AND t.createdDate BETWEEN :startDate AND :endDate")
    List<Transaction> findAllByMemberAndPeriod(@Param("member") Member member,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);

}
