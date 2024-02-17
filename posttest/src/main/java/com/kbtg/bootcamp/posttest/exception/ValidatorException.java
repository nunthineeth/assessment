//package com.kbtg.bootcamp.posttest.exception;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//@ResponseStatus(HttpStatus.BAD_REQUEST)
//public class ValidatorException extends RuntimeException {
//
//    public ValidatorException(String message) {
//        super(message);
//    }
//
//    public static ValidatorException required(String filedName) {
//        return new ValidatorException(filedName + " field is required");
//    }
//
//    public static ValidatorException invalid(String filedName) {
//        return new ValidatorException(filedName + " is invalid");
//    }
//
//    public static ValidatorException invalidStepOperation() {
//        return new ValidatorException("Invalid step operation");
//    }
//
//    public static ValidatorException parameterMissingOrInvalid() {
//        return new ValidatorException("Parameter Missing or Invalid");
//    }
//}
