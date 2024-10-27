package com.wow.libre.infrastructure.controller;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.port.in.transaction.*;
import com.wow.libre.domain.shared.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.wow.libre.domain.constant.Constants.HEADER_TRANSACTION_ID;
import static com.wow.libre.domain.constant.Constants.HEADER_USER_ID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionPort transactionPort;

    public TransactionController(TransactionPort transactionPort) {
        this.transactionPort = transactionPort;
    }

    @GetMapping
    public ResponseEntity<GenericResponse<TransactionsDto>> transactions(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestParam final Integer size,
            @RequestParam final Integer page) {

       TransactionsDto  transactions = transactionPort.transactionsByUserId(userId, page, size, transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(transactions, transactionId).created().build());
    }
}
