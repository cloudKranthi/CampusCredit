package com.college.wallet.service;
import java.math.BigDecimal;
import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.college.wallet.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WalletService {

    private final RedisTemplate<String,Object> redisTemplate;
    private final MoneyTransferService moneyTransferService;


    public void checkToken(String token){
      String setToken="set:Lock:"+token;
    Boolean isNew =redisTemplate.opsForValue().setIfAbsent(setToken,"PROCCESSING",Duration.ofMinutes(5));
    if(Boolean.FALSE.equals(isNew)){
      throw new BusinessException("Duplicate request",HttpStatus.CONFLICT);
    }
    }
//Atomic safety 
    public  void transferMoney(String senderPhonenumber,String ReceiverUserPhonenumber,BigDecimal Amount,String idempotencyKey){
         
      try{
        checkToken(idempotencyKey);
         moneyTransferService.moneyTransfer(senderPhonenumber,ReceiverUserPhonenumber,Amount,idempotencyKey);
        }
      catch(Exception e){
                  String token="set:Lock:"+idempotencyKey;
                  redisTemplate.delete(token);
                  throw new BusinessException("Transaction failed",HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
}
