package com.college.wallet.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="valuts")
public class valuts extends BaseEntity {
    @Column(name="parent_phone_number")
    private String parent_phone_number;
    @Column(name="student_phone_number")
    private String strudent_phone_number;
    @Column(name="daily_Limit",precision=19,scale=4)
    private BigDecimal dailyLimit;
    @Column(name="current_balance",precision=19,scale=4)
    private BigDecimal currentbalance;

}
