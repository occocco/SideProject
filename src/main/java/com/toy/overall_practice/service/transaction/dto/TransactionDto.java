package com.toy.overall_practice.service.transaction.dto;

import com.toy.overall_practice.domain.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

    private String sender;
    private String receiver;
    private Long amount;

    private String createdDate;

    public TransactionDto(String sender, String receiver, Long amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    public static TransactionDto toTransactionDto(Transaction tx) {
        return new TransactionDto(
                tx.getSender().getLoginId(),
                tx.getReceiver().getLoginId(),
                tx.getAmount(),
                tx.getCreatedDate().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm"))
        );
    }
}
