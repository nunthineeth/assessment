package com.kbtg.bootcamp.posttest.securityconfig;

import com.kbtg.bootcamp.posttest.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(Http401UnauthorizedEntryPoint.class);

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public JwtAuthFilter(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTHORIZATION);
         String username = null;
         String jwtToken;

        if(authHeader == null || !authHeader.startsWith("Bearer")){
            filterChain.doFilter(request, response); //pass to next filter
            return;
        }

        jwtToken = authHeader.substring(7);
        try {
            username = jwtService.extractUsername(jwtToken);
        } catch (ExpiredJwtException e) {
            log.warn("the token is expired and not valid anymore");
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
//            UserDetails userDetails = userRepository.findUserByUsername(username); //ให้เชื่อจาก token เลย

            List<GrantedAuthority> authorities = jwtService.getAuthorities(jwtToken);
            if(jwtService.validateToken(jwtToken)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            //วิธีนี้ดีตรงที่ว่า แต่ละ microservice ไม่ต้องต่อ user repository ก็ได้ แค่เช้คจาก token พอ
        }

        filterChain.doFilter(request, response);
    }
}
