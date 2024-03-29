package com.toy.overall_practice.domain.member.repository;

import com.toy.overall_practice.domain.member.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = {"role"})
    Optional<Member> findByLoginId(String loginId);

    @EntityGraph(attributePaths = {"wallet"})
    Optional<Member> findWalletByLoginId(String loginId);
}
