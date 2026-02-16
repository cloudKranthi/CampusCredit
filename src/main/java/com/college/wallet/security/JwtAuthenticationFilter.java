package com.college.wallet.security;

import com.college.wallet.model.User; // Use YOUR entity
import com.college.wallet.repository.UserRepository;
import com.college.wallet.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie; // ADDED: Missing import
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull; // Standard Spring NonNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepositry;
    @Override
protected  void  doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain)throws ServletException,IOException{
 String jwt=null;
 for(Cookie cookie:request.getCookies()){
    if(("accessToken").equals(cookie.getName())){
        jwt=cookie.getValue();
        break;
    }
 }
 if(jwt==null){
    filterChain.doFilter(request, response);
    return;
 }
  try {
      String userId=jwtService.findUserId(jwt);
      User user=userRepositry.findById(Long.parseLong(userId)).orElse(null);
      if(user!=null &&jwtService.checkToken(jwt,user)){
        if(SecurityContextHolder.getContext().getAuthentication()==null){
            UsernamePasswordAuthenticationToken authtoken= new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
            authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authtoken);
        }
      }
  } catch (Exception e) {
    logger.error("Could not set User Authentication");
  }
  filterChain.doFilter(request,response);
}
}
