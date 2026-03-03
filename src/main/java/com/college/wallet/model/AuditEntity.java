package com.college.wallet.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AuditEntity  extends BaseEntity{

    private String phoneNumber;
    private String action;
    private String status;
    private String description;
    private String ip;
    private  String details;

}