package com.college.wallet.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import com.college.wallet.model.User;
import com.college.wallet.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/users")
public class UserController{
    @Autowired
    private UserRepository userRepository;
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user){
        User savedUser=userRepository.save(user);
        return new ResponseEntity<> (savedUser,HttpStatus.CREATED);
    }
    
}