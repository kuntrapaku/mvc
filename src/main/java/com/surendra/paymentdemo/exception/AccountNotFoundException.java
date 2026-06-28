package com.surendra.paymentdemo.exception; // exception package

public class AccountNotFoundException extends RuntimeException { // custom unchecked exception

    public AccountNotFoundException(String message) { // pass error message from service
        super(message); // send message to parent RuntimeException
    }
}