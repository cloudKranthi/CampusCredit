package com.college.wallet.Controller;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.college.wallet.service.WalletService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionContoller {
    private final WalletService walletService;
    @PostMapping("/money-transfer")
    public ResponseEntity<?> moneyTransfer(@Valid @RequestBody String senderemail,String receiveremail,BigDecimal Amount,WalletService walletService){
        walletService.transferMoney(senderemail, receiveremail, Amount, receiveremail);
        return ResponseEntity.status(HttpStatus.CREATED).body("Transaction succesfull");
    }
}
