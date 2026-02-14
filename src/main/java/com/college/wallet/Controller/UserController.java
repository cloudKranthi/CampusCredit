package com.college.wallet.Controller;

import java.net.http.HttpHeaders;

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

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.college.wallet.service.JwtService;

import java.util.Map;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;


@RestController
@RequestMapping("/api/users")
public class UserController{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
        
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user){
        String hashedPassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        User savedUser=userRepository.save(user);
        return new ResponseEntity<> (savedUser,HttpStatus.CREATED);
    }

   @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequestUser){
          
    if(!loginRequestUser){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("insufficeint data")
    }
        if(passwordEncoder.matches(loginRequestUser.getPassword(),user.getPassword())){
           Map<String,String>tokens= jwtService.tokens(user.getId());
           String accessToken=tokens.get("accessToken");
           String refreshToken=tokens.get("refreshToken");
           user.setRefreshToken(refreshToken);
           userRepository.save(user);
           ResponseCookie responseCookie=ResponseCookie.from("accessToken",accessToken)
           .httpOnly(true).secure(false).sameSite("Strict").build();
            return  ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,responseCookie.toString()).body("Logged in succesfully");
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Password");
        
    }        .orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found "));
    }
}
