package com.college.wallet.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; 

import com.college.wallet.model.Transaction;



public interface TransactionHistoryRepository extends JpaRepository<Transaction,UUID> {
    @Query("SELECT t FROM Transaction t WHERE t.sentBy.user.phoneNumber=:phoneNumber "+" OR "+" t.receivedBy.user.phoneNumber=:phoneNumber "+"ORDER BY t.createdAt "+" DESC ")
    Page<Transaction> findByHistory(@Param("phoneNumber") String phoneNumber, Pageable pageable);
}
