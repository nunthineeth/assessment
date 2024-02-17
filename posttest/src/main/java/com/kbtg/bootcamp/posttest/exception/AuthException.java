//package com.kbtg.bootcamp.posttest.exception;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//@ResponseStatus(HttpStatus.UNAUTHORIZED)
//public class AuthException extends RuntimeException {
//
//    public AuthException(String code) {
//        super(code);
//    }
//
//    public static AuthException expired() {
//        return new AuthException("Token has expired");
//    }
//
//    public static AuthException unauthorized() {
//        return new AuthException("Unauthorized");
//    }
//
//    public static AuthException loginFail() {
//        return new AuthException("Too many failed login");
//    }
//}
