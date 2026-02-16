package com.college.wallet.repository;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.college.wallet.model.User;

@Repository
public interface  UserRepository extends JpaRepository<User,UUID>{
    User  getByEmail(String email);
    User  findUserById(UUID id);
}
