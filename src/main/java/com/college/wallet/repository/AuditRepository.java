package com.college.wallet.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.college.wallet.model.AuditEntity;

@Repository
public interface AuditRepository extends JpaRepository<AuditEntity,UUID> {
    Optional<AuditEntity> findByPhoneNumber(String phoneNumber);
}
