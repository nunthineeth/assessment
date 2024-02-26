package com.kbtg.bootcamp.posttest.exception;

public class PersistenceFailureException extends RuntimeException {
    public PersistenceFailureException(String message) {
        super(message);
    }
}
