package com.toy.overall_practice.api.transaction;

import com.toy.overall_practice.domain.transaction.Transaction;
import com.toy.overall_practice.service.transaction.dto.TransactionDto;
import com.toy.overall_practice.service.transaction.TransactionService;
import com.toy.overall_practice.service.transaction.dto.TransactionLogDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Api(value = "거래 REST API", tags = {"Tx REST API"})
@Slf4j
@RestController
@RequiredArgsConstructor
public class TxRestController {

    private final TransactionService transactionService;
    @ApiOperation(value = "거래 저장", notes = "회원 간 거래 시 거래 엔티티를 저장하는 API")
    @PostMapping("/transactions")
    public ResponseEntity<TransactionDto> createTx(@RequestBody TransactionDto transactionDto) {
        Transaction tx = transactionService.createTransaction(transactionDto);
        TransactionDto txDto = TransactionDto.toTransactionDto(tx);
        return ResponseEntity.ok().body(txDto);
    }

    @ApiOperation(value = "거래 내역 조회", notes = "회원의 거래 내역을 조회하는 API")
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionLogDto>> getMyTxRecord(Principal principal) {
        List<TransactionLogDto> txRecord = transactionService.findTxRecord(principal.getName());
        return ResponseEntity.ok().body(txRecord);
    }

}
