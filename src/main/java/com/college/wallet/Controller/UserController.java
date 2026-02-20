package com.college.wallet.Controller;

import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.college.wallet.dto.UserResponse;
import com.college.wallet.model.User;
import com.college.wallet.repository.UserRepository;
import com.college.wallet.service.JwtService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController{
    
    private final UserRepository userRepository;
    
    private final BCryptPasswordEncoder passwordEncoder;
    
    private final JwtService jwtService;
        
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody User user){
        String hashedPassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        User savedUser=userRepository.save(user);
        UserResponse userResponse= new UserResponse(savedUser.getId(),savedUser.getUsername(),savedUser.getEmail());
        return new ResponseEntity<> (userResponse,HttpStatus.CREATED);
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
              UserResponse userResponse= new UserResponse(user.getId(),user.getUsername(),user.getEmail());
            Map<String,Object> map=  Map.of("User Logged in succesfully",userResponse);
      ResponseCookie cookies= ResponseCookie.from("accessToken",Objects.requireNonNull(accessToken)).httpOnly(true).secure(false).path("/").sameSite("Strict").build();
      return ResponseEntity.ok().header("Set-Cookie",cookies.toString()).body(map);
    }
    else{
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid passowrd");
    }
  }    
  
}

