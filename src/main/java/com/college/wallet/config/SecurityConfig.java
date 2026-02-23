package com.college.wallet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.college.wallet.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig{
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
@Bean
public BCryptPasswordEncoder passwordencoder(){
    return new  BCryptPasswordEncoder();
}
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
  http
.csrf(csrf->csrf.disable())
.authorizeHttpRequests(auth->auth
.requestMatchers("/api/users/login","/api/users/register").permitAll()
.requestMatchers("/user/").hasAnyRole("ADMIN","USER")
.requestMatchers("/admin/").hasRole("ADMIN")
.anyRequest().authenticated()).addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);
return http.build();
}

}

