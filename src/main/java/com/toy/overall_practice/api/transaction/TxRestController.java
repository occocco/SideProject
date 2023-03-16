package com.toy.overall_practice.api.transaction;

import com.toy.overall_practice.domain.transaction.Transaction;
import com.toy.overall_practice.service.transaction.dto.TransactionDto;
import com.toy.overall_practice.service.transaction.TransactionService;
import com.toy.overall_practice.service.transaction.dto.TransactionLogDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TxRestController {

    private final TransactionService transactionService;

    @PostMapping("/txs")
    public ResponseEntity<TransactionDto> createTx(@RequestBody TransactionDto transactionDto) {
        Transaction tx = transactionService.createTransaction(transactionDto);
        TransactionDto txDto = TransactionDto.toTransactionDto(tx);
        return ResponseEntity.ok().body(txDto);
    }

    @GetMapping("/txs/{id}")
    public ResponseEntity<List<TransactionLogDto>> getMyTxRecord(@PathVariable String id) {
        List<TransactionLogDto> txRecord = transactionService.findTxRecord(id);
        return ResponseEntity.ok().body(txRecord);
    }
}
