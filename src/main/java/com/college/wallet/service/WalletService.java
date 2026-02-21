package com.college.wallet.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.college.wallet.model.Purse;
import com.college.wallet.model.Transaction;
import com.college.wallet.model.TransactionStatus;
import com.college.wallet.model.User;
import com.college.wallet.repository.PurseRepository;
import com.college.wallet.repository.TransactionRepositry;
import com.college.wallet.repository.UserRepository;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class WalletService {
    private final TransactionRepositry transactionRepositry;
    private  final UserRepository userRepository;
    private   final PurseRepository purseRepository;
    @Transactional//Atomic safety 
    public  void transferMoney(String senderemail,String ReceiverUserEmail,BigDecimal Amount,String idempotencyKey){
         if(transactionRepositry.findByIdempotancyKey(idempotencyKey).isPresent()){return ;}//versioning
           User senderUser=userRepository.getByEmail(senderemail);
           User receiverUser=userRepository.getByEmail(ReceiverUserEmail);
                Purse senderspurse=purseRepository.findById(senderUser.getId()).orElseThrow(()->new RuntimeException("no such sender user present"));
                Purse receiverpurse=purseRepository.findById(receiverUser.getId()).orElseThrow(()-> new RuntimeException("no such receiver user is present"));
                if(("ACTIVE").equals(senderspurse.getStatus().toString())){
                      throw new RuntimeException("Account is not in Active");
                    
                }
                    
                else if(("INACTIVE").equals(receiverpurse.getStatus().toString())){
                    throw new RuntimeException("Account is not in Active");
                      
                }
                else if(senderspurse.getBalance().compareTo(Amount)<0){
                  throw new RuntimeException("Insufficient balance");
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

           }

         
    }

