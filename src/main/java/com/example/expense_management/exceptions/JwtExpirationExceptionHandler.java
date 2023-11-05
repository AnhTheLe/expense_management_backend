package com.example.expense_management.exceptions;

public class JwtExpirationExceptionHandler extends RuntimeException{
    public JwtExpirationExceptionHandler(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
