package com.college.wallet.service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys; // Corrected Import

@Service
public class JwtService{
    @Value("${app.jwt.secret}")
    private String SecretKey;
    @Value("${app.jwt.access-token-expiry}")
    private long  atexpiry;
    @Value("${app.jwt.refresh-token-expiry}")
    private long rtexpiry;
    public Map<String,String> tokens(UUID userId){
        String at=Jwts.builder()
                  .setSubject(userId.toString()).setIssuedAt(new Date())
                  .setExpiration(new Date(System.currentTimeMillis()+atexpiry))
                 . signWith(Keys.hmacShaKeyFor(SecretKey.getBytes()),SignatureAlgorithm.HS256)
                 .compact();
        String rt=Jwts.builder().setSubject(userId.toString()).setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis()+rtexpiry)).signWith(Keys.hmacShaKeyFor(SecretKey.getBytes()),SignatureAlgorithm.HS256).compact();
       return  Map.of("accessToken",at,"refreshToken",rt);
    }

}