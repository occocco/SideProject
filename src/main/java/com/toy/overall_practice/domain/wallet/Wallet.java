package com.toy.overall_practice.domain.wallet;

import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.service.wallet.dto.WalletCreateForm;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private Long id;

    @OneToOne(fetch = LAZY, mappedBy = "wallet")
    private Member member;

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
        Wallet wallet = new Wallet(form.getMember());
        wallet.name = form.getWalletName();
        wallet.member = form.getMember();
        form.getMember().ConnectWallet(wallet);
        return wallet;
    }

    private Wallet(Member member) {
        this.member = member;
        this.balance = 0L;
    }
}
