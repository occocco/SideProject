package com.toy.overall_practice.exception;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(String message) {
        super(message);
    }
}
