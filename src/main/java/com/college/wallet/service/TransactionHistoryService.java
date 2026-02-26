 package com.college.wallet.service;
import com.college.wallet.model.Transaction;
import java.math.BigDecimal;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.college.wallet.dto.TransactionHistorydto;
import com.college.wallet.repository.TransactionHistoryRepository;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class TransactionHistoryService {
    private final TransactionHistoryRepository transactionHistoryRepository;
    public Page<TransactionHistorydto> getTransactionHistory(String phoneNumber,Pageable pageable){
        Page<Transaction>tx= transactionHistoryRepository.findByHistory(phoneNumber,pageable);
        return tx.map(t->{
            boolean isSentUser=t.getSentBy().getUser().getPhonenumber().equals(phoneNumber);
            String partnerPhoneNumber=isSentUser?t.getReceivedBy().getUser().getPhonenumber():t.getSentBy().getUser().getPhonenumber();
            String partnerUsername=isSentUser?t.getReceivedBy().getUser().getUsername():t.getSentBy().getUser().getUsername();
            BigDecimal Amount=isSentUser?t.getAmount().negate():t.getAmount();
            String TransactionType=isSentUser?"SENT":"RECEIVED";
            return new TransactionHistorydto(partnerPhoneNumber,partnerUsername,phoneNumber,Amount,TransactionType,t.getCreatedAt());
        }
        );
    }
}
