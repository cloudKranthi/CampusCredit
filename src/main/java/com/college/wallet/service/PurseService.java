package com.college.wallet.service;

import org.springframework.stereotype.Service;
import java.util.UUID;
import com.college.wallet.model.Purse;
import com.college.wallet.model.User;
import com.college.wallet.repository.PurseRepository;
import com.college.wallet.repository.UserRepository;
import com.college.wallet.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class PurseService {
    private final PurseRepository purseRepository;
    private final JwtService jwtService;
    public Purse getPurseByUser(){
    String phoneNumber=SecurityContextHolder.getContext().getAuthentication().getName();
    Purse purse=purseRepository.findByUserPhonenumber(phoneNumber);
    return purse;
    }
    public boolean addMoney(BigDecimal amount){
        try{
        Purse purse=getPurseByUser();
        purse.setBalance(purse.getBalance().add(amount));
        purseRepository.save(purse);
        return true;
        }catch(Exception e){
            return false;
        }
    }
}
