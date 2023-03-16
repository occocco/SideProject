package com.toy.overall_practice.domain.transaction;

import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.domain.wallet.Wallet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "sender_id", referencedColumnName = "member_id")
    private Member sender;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "receiver_id", referencedColumnName = "member_id")
    private Member receiver;

    private Long amount;

    @CreationTimestamp
    @Column(name = "created_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;

    public static Transaction createTx(Member sender, Member receiver, Long amount) {
        Wallet senderWallet = sender.getWallet();
        Wallet receiverWallet = receiver.getWallet();

        if (sender.equals(receiver)) {
            receiverWallet.addBalance(amount);
        } else {
            senderWallet.removeBalance(amount);
            receiverWallet.addBalance(amount);
        }

        return new Transaction(sender, receiver, amount);
    }

    private Transaction(Member sender, Member receiver, Long amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

}
