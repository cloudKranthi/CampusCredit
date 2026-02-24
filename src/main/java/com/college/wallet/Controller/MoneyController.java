package com.college.wallet.Controller;
import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import com.college.wallet.service.PurseService;

import lombok.RequiredArgsConstructor;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/money")
public class MoneyController {
    private final PurseService purseService;
    @PostMapping("/addmoney")
    public ResponseEntity<?> addMoney(@RequestParam BigDecimal amount){
        boolean success=purseService.addMoney(amount);
        if(!success){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add money");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Money added successfully");
    }
}
