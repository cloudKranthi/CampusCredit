package com.college.wallet.Controller;

import java.math.BigDecimal;
import com.college.wallet.dto.TransactionHistorydto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import com.college.wallet.service.WalletService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.college.wallet.service.TransactionHistoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import static org.springframework.data.domain.Sort.by;
@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionContoller {
    private final WalletService walletService;
    private final TransactionHistoryService transactionHistoryService;
    @PostMapping("/money-transfer")
    public ResponseEntity<?> moneyTransfer(@Valid @RequestBody String senderemail,String receiveremail,BigDecimal Amount,WalletService walletService){
        walletService.transferMoney(senderemail, receiveremail, Amount, receiveremail);
        return ResponseEntity.status(HttpStatus.CREATED).body("Transaction succesfull");
    }
    @GetMapping("/history")
    public ResponseEntity<Page<TransactionHistorydto>> getTransactionHistory(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int limit,
        @RequestParam String phoneNumber
    ) {
        Pageable pageable = PageRequest.of(page, limit,Sort.by("createdAt").descending());
        Page<TransactionHistorydto> history = transactionHistoryService.getTransactionHistory(phoneNumber, pageable);
        return ResponseEntity.ok(history);
    }
}
