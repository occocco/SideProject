package com.toy.overall_practice.domain.wallet;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet,Long> {
    @EntityGraph(attributePaths = "member")
    Optional<Wallet> findById(Long id);
}
