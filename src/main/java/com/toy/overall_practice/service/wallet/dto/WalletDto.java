package com.toy.overall_practice.service.wallet.dto;

import com.toy.overall_practice.domain.wallet.Wallet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletDto {

    private String name;

    private Long balance;

    private String createdDate;

    private String updatedDate;

    public static WalletDto toWalletDto(Wallet wallet) {
        return new WalletDto(
                wallet.getName(),
                wallet.getBalance(),
                wallet.getCreatedDate().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm")),
                wallet.getCreatedDate().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm"))
        );
    }
}
