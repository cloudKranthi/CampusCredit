package com.college.wallet.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.util.UUID;
import jakarta.persistence.*;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
@Entity
@Table(name="purses")
public class  Purse{
@Id
@GeneratedValue(strategy=GenerationType.UUID)
private UUID id;
@OneToOne(fetch=FetchType.LAZY)
@JoinColumn(name= "user_id")
private User user;
@Column(precision=19,scale=4)
private BigDecimal balance =BigDecimal.ZERO;
private String CardDetailsEncryString;
@Enumerated(EnumType.STRING)
@Column(nullable=false)
private  WalletStatus status=WalletStatus.INCOMPLETE;


@Version
private Long  version;
@CreationTimestamp
@Column(updatable=false)
private LocalDateTime createdAt;
@UpdateTimestamp
private LocalDateTime updatedAt;
}