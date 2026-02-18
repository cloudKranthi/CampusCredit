package com.college.wallet.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.college.wallet.model.Purse;
@Repository
public interface PurseRepository extends JpaRepository<Purse,UUID> {
    Optional<Purse> findById(UUID id);
}
