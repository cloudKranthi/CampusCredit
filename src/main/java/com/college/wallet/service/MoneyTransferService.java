package com.college.wallet.service;

import java.math.BigDecimal;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import com.college.wallet.exception.BusinessException;
import com.college.wallet.model.Purse;
import com.college.wallet.model.Transaction;
import com.college.wallet.model.TransactionStatus;
import com.college.wallet.model.User;
import com.college.wallet.repository.PurseRepository;
import com.college.wallet.repository.TransactionRepositry;
import com.college.wallet.repository.UserRepository;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
public class MoneyTransferService {
        private final TransactionRepositry transactionRepositry;
    private  final UserRepository userRepository;
    private   final PurseRepository purseRepository;
    private final RedisTemplate<String,Object> redisTemplate;
    @Transactional
             public void moneyTransfer(String senderPhonenumber,String ReceiverUserPhonenumber,BigDecimal Amount,String idempotencyKey){
      
         if(transactionRepositry.findByIdempotancyKey(idempotencyKey).isPresent()){return ;}//versioning
           User senderUser=userRepository.findByPhonenumber(senderPhonenumber);
           User receiverUser=userRepository.findByPhonenumber(ReceiverUserPhonenumber);
                Purse senderspurse=purseRepository.findByUserPhonenumber(senderUser.getPhonenumber()).orElseThrow(()->new BusinessException("no such sender user present",HttpStatus.NOT_FOUND));
                Purse receiverpurse=purseRepository.findByUserPhonenumber(receiverUser.getPhonenumber()).orElseThrow(()-> new BusinessException("no such receiver user is present",HttpStatus.NOT_FOUND));
                if("INACTIVE".equals(senderspurse.getStatus().name())){
                      throw new BusinessException("Account is not in Active",HttpStatus.FORBIDDEN);
                    
                }
                    
                else if(("INACTIVE").equals(receiverpurse.getStatus().name())){
                    throw new BusinessException("Account is not in Active",HttpStatus.FORBIDDEN);
                      
                }
                else if(senderspurse.getBalance().compareTo(Amount)<0){
                  throw new BusinessException("Insufficient balance",HttpStatus.BAD_REQUEST);
                }
                Transaction tx=new Transaction();
                senderspurse.setBalance(senderspurse.getBalance().subtract(Amount));
                receiverpurse.setBalance(receiverpurse.getBalance().add(Amount));
                tx.setIdempotencyKey(idempotencyKey);
                tx.setSentBy(senderspurse);
                tx.setReceivedBy(receiverpurse);
                tx.setAmount(Amount);
                tx.setStatus(TransactionStatus.COMPLETED);
                purseRepository.save(senderspurse);
                purseRepository.save(receiverpurse);
                transactionRepositry.save(tx);
                try{
                    String senderCacheKey="balance:phoneNumber:"+senderUser.getPhonenumber();
                    String receiverCacheKey="balance:phoneNumber:"+receiverUser.getPhonenumber();
                    redisTemplate.delete(senderCacheKey);
                    redisTemplate.delete(receiverCacheKey);
                }catch(Exception e){
                    System.out.println("Error in caching redis");
                }
              }
    }



