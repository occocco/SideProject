package com.toy.overall_practice.service.transaction.dto;

import com.toy.overall_practice.domain.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionLogDto {

    private String sender;
    private String receiver;
    private Long amount;
    private Long balance;
    private String createdDate;

    public static TransactionLogDto toTransactionDto(Transaction tx, Long balance) {
        return new TransactionLogDto(
                tx.getSender().getLoginId(),
                tx.getReceiver().getLoginId(),
                tx.getAmount(),
                balance,
                tx.getCreatedDate().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm"))
        );
    }
}
