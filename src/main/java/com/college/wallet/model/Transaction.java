package com.college.wallet.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name="Transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    @Column(unique=true,nullable=false)
    private UUID id;
    @Column(unique=true,nullable=false,updatable=false)
    private String idempotencyKey;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="sent_id",nullable=false)
    private Purse sentBy;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="receiver_id",nullable=false)
    private Purse receivedBy;
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private TransactionStatus status =TransactionStatus.PENDING;
    @Column(nullable=false,precision=19,scale=4)
    private BigDecimal amount;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private String EncryptedData;
}
