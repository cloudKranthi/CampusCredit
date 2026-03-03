package com.college.wallet.service;

import org.springframework.stereotype.Service;

import com.college.wallet.model.AuditEntity;
import com.college.wallet.repository.AuditRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditService {
    private final AuditRepository auditRepository;
    public void logAudit(String phoneNumber,String action,String Status,String des,String ip){
        AuditEntity auditEntity=new AuditEntity();
        auditEntity.setPhoneNumber(phoneNumber);
        auditEntity.setAction(action);
        auditEntity.setStatus(Status);
        auditEntity.setDescription(des);
        auditEntity.setIp(ip);

        auditRepository.save(auditEntity);
    }
}
