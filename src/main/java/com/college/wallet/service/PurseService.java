package com.college.wallet.service;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.college.wallet.exception.BusinessException;
import com.college.wallet.model.Purse;
import com.college.wallet.repository.PurseRepository;
import com.college.wallet.repository.UserRepository;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class PurseService {
    private final PurseRepository purseRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    public Purse getPurseByUser(){
    UUID uuid=UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
    String phoneNumber=userRepository.findById(uuid).orElseThrow(()->new BusinessException("User not found", HttpStatus.NOT_FOUND)).getPhonenumber();
    return purseRepository.findByUserPhonenumber(phoneNumber).orElseThrow(()->new BusinessException("Purse not found for user", HttpStatus.NOT_FOUND));
    }
    public String getPhoneNumber(){
        UUID uuid=UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        return userRepository.findById(uuid).orElseThrow(()->new BusinessException("User not found", HttpStatus.NOT_FOUND)).getPhonenumber();
    }
    public boolean addMoney(BigDecimal amount){
        try{

        Purse purse=getPurseByUser();
        purse.setBalance(purse.getBalance().add(amount));
        purseRepository.save(purse);
        try{
         String cacheKey="balance:phoneNumber:"+getPhoneNumber();
        redisTemplate.delete(cacheKey);
        }catch(Exception e){
            System.err.println("Error deleting cache: " + e.getMessage());
        }
        return true;
        }catch(Exception e){
            return false;
        }
    }
    public BigDecimal getBalance(){
    String phoneNumber=getPhoneNumber();
    String cacheKey="balance:phoneNumber:"+phoneNumber;
        try{
         Object cached=redisTemplate.opsForValue().get(cacheKey);
         if(cached!=null){
            return new BigDecimal(cached.toString());
         }
         else{
            return getBalanceFromDb(phoneNumber, cacheKey);
         }
        }catch(Exception e){
          System.out.println("Error retrieving cached balance: " + e.getMessage());
                      return getBalanceFromDb(phoneNumber, cacheKey);
        }
    }
    public BigDecimal getBalanceFromDb(String phoneNumber,String cacheKey){
         BigDecimal balance=purseRepository.findByUserPhonenumber(phoneNumber).orElseThrow(()->new BusinessException("Purse not found for user", HttpStatus.NOT_FOUND)).getBalance();
             try{
                redisTemplate.opsForValue().set(cacheKey, balance, Duration.ofMinutes(10));
             }catch(Exception e){
                System.out.println("Error while setting redis");
                return balance;
             }
             return balance;
    }
}