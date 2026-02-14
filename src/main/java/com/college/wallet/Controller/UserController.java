package com.college.wallet.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.college.wallet.model.User;
import com.college.wallet.repository.UserRepository;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/users")
public class UserController{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user){
        String hashedPassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        User savedUser=userRepository.save(user);
        return new ResponseEntity<> (savedUser,HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequestUser){
        return userRepository.findByEmail(loginRequestUser.getEmail())
          .map(user->{
        if(passwordEncoder.matches(loginRequestUser.getPassword(),user.getPassword())){
            return  ResponseEntity.ok("Logged in succesfully");
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Password");
        
    }})
        .orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found "));
    }
}