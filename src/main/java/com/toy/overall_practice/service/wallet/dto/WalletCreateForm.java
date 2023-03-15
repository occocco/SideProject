package com.toy.overall_practice.service.wallet.dto;

import com.toy.overall_practice.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletCreateForm {

    private String walletName;
    private Member member;

}
