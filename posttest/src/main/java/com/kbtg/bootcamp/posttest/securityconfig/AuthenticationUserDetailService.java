package com.kbtg.bootcamp.posttest.securityconfig;

import com.kbtg.bootcamp.posttest.repository.UserRepository;
import com.kbtg.bootcamp.posttest.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationUserDetailService implements UserDetailsService {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthenticationUserDetailService(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findUserByUsername(username);
    }

    public String generateJwt(String username) {
        CustomUserDetail userDetails = userService.findUserByUsername(username);
        return jwtService.generateToken(userDetails);
    }
}
