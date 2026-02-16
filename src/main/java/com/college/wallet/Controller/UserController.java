package com.college.wallet.Controller;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.college.wallet.model.User;
import com.college.wallet.repository.UserRepository;
import com.college.wallet.service.JwtService;

import jakarta.validation.Valid;


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
  public ResponseEntity<?> loginUser(@Valid @RequestBody User loginRequestUser){
    User user= userRepository.getByEmail(loginRequestUser.getEmail());
    if(user==null){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such User found");
    }
    if(passwordEncoder.matches(loginRequestUser.getPassword(),user.getPassword())){
        Map<String,String> tokens= jwtService.tokens(user.getId());
      String accessToken = tokens.getOrDefault("accessToken","");
      String refreshToken = tokens.getOrDefault("refreshToken","");
      user.setRefreshToken(refreshToken);
      userRepository.save(user);
      ResponseCookie cookies= ResponseCookie.from("accessToken",Objects.requireNonNull(accessToken)).httpOnly(true).secure(false).path("/").sameSite("Strict").build();
      return ResponseEntity.ok().header("Set-Cookie",cookies.toString()).body("User logged in sucessfully");
    }
    else{
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid passowrd");
    }
  }    
  
}

