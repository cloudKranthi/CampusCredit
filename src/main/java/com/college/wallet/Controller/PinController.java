package com.college.wallet.Controller;

import org.springframework.web.bind.annotation.RestController;
import com.college.wallet.dto.PinChangeResponse;
import com.college.wallet.service.PurseService;
import com.college.wallet.dto.pinChangeAdminResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pin")
public class PinController {
    private final PurseService purseService;
    @PutMapping("/change-pin")
    public ResponseEntity<?> pinchangeController(@Valid @RequestBody PinChangeResponse pinChangeResponse ){
    
        purseService.setTransactionPin(pinChangeResponse.getOldpin(), pinChangeResponse.getNewpin());
        return ResponseEntity.ok("Transaction pin changed");
    }
    @PutMapping("/admin/phoneNumber")
    public ResponseEntity<?>pinchangeAdminController(@Valid @RequestBody pinChangeAdminResponse pinChangeAdminResponse ){
        purseService.SaveNewPin(pinChangeAdminResponse.getPhonenumber(), pinChangeAdminResponse.getPin());
        return ResponseEntity.ok("Transaction pin changed ");
    }
}
