package com.toy.overall_practice.domain.wallet;

import com.toy.overall_practice.exception.InsufficientFundsException;
import com.toy.overall_practice.service.wallet.dto.WalletCreateForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.persistence.Entity;

import java.time.LocalDateTime;

import static lombok.AccessLevel.*;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = PROTECTED)
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private Long id;

    @Column(name = "wallet_name")
    @ColumnDefault(value = "'My Wallet'")
    private String name;

    @ColumnDefault(value = "'0'")
    private Long balance;

    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updatedDate;

    public static Wallet createWallet(WalletCreateForm form) {
        Wallet wallet = new Wallet(form.getWalletName(), 0L);
        form.getMember().ConnectWallet(wallet);
        return wallet;
    }

    public void addBalance(Long amount) {
        this.balance += amount;
    }

    public void removeBalance(Long amount) {
        long alterBalance = this.balance - amount;
        if (alterBalance < 0) {
            throw new InsufficientFundsException("잔액이 부족합니다.");
        }
        this.balance = alterBalance;
    }

    private Wallet(String name, Long balance) {
        this.name = name;
        this.balance = balance;
    }
}
