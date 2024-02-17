//package com.kbtg.bootcamp.posttest.exception;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//public class CRUDException extends RuntimeException {
//
//    public CRUDException(String message) {
//        super(message);
//    }
//
//    public static CRUDException saveFailed(String msg) {
//        return new CRUDException(msg+", Save failed.");
//    }
//}
