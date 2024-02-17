//package com.kbtg.bootcamp.posttest.exception;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//@ResponseStatus(HttpStatus.BAD_REQUEST)
//public class BadRequestException extends RuntimeException {
//
//    public BadRequestException(String message) {
//        super(message);
//    }
//
//    public static BadRequestException duplicate(String msg) {
//        return new BadRequestException(msg+"is duplicate");
//    }
//
//    public static BadRequestException alreadyUsed(String msg) {
//        return new BadRequestException(msg+" has already been used");
//    }
//
//    public static BadRequestException alreadyExists(String msg) {
//        return new BadRequestException(msg+" already exists");
//    }
//
//}
