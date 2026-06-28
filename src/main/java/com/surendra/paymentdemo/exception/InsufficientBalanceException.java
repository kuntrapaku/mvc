package com.surendra.paymentdemo.exception; // exception package

public class InsufficientBalanceException extends RuntimeException { // payment fails when balance is low

    public InsufficientBalanceException(String message) { // receive message
        super(message); // store message in exception
    }
}