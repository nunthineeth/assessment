package com.kbtg.bootcamp.posttest.controller;

import com.kbtg.bootcamp.posttest.dto.UserAuthenticationRequest;
import com.kbtg.bootcamp.posttest.securityconfig.AuthenticationUserDetailService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final AuthenticationUserDetailService authenticationUserDetailService;

    public AuthenticationController(AuthenticationManager authenticationManager, AuthenticationUserDetailService authenticationUserDetailService) {
        this.authenticationManager = authenticationManager;
        this.authenticationUserDetailService = authenticationUserDetailService;
    }

    @PostMapping("/authenticate")
    public String authentication(@RequestBody UserAuthenticationRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        return authenticationUserDetailService.generateJwt(request.username());
    }
}
