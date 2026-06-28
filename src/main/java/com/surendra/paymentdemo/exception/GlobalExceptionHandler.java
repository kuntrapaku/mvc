package com.surendra.paymentdemo.exception; // exception package

import jakarta.servlet.http.HttpServletRequest; // gives api path
import org.springframework.http.HttpStatus; // http status codes
import org.springframework.http.ResponseEntity; // response body + status
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler; // catches exception
import org.springframework.web.bind.annotation.RestControllerAdvice; // global rest error handler


import java.time.LocalDateTime; // error time

@RestControllerAdvice // catches exceptions from all controllers
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class) // handles account missing
    public ResponseEntity<ErrorResponse> handleAccountNotFound(
            AccountNotFoundException ex, // actual exception object
            HttpServletRequest request // gives current api path
    ) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(), // current time
                HttpStatus.NOT_FOUND.value(), // 404
                "account not found", // error title
                ex.getMessage(), // message from service
                request.getRequestURI() // api path
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error); // return 404 json
    }

    @ExceptionHandler(InsufficientBalanceException.class) // handles low balance
    public ResponseEntity<ErrorResponse> handleInsufficientBalance(
            InsufficientBalanceException ex, // actual exception
            HttpServletRequest request // current api path
    ) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(), // current time
                HttpStatus.BAD_REQUEST.value(), // 400
                "insufficient balance", // error title
                ex.getMessage(), // actual message
                request.getRequestURI() // api path
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error); // return 400 json
    }

    @ExceptionHandler(Exception.class) // handles unknown errors
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, // unknown exception
            HttpServletRequest request // current api path
    ) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(), // current time
                HttpStatus.INTERNAL_SERVER_ERROR.value(), // 500
                "internal server error", // error title
                "something went wrong", // don't expose internal details
                request.getRequestURI() // api path
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error); // return 500 json
    }
    @ExceptionHandler(MethodArgumentNotValidException.class) // handles @Valid errors
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, // validation exception object
            HttpServletRequest request // current api path
    ) {

        String message = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage(); // first validation error

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(), // current time
                HttpStatus.BAD_REQUEST.value(), // 400
                "validation failed", // error title
                message, // validation message
                request.getRequestURI() // api path
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error); // return 400 json
    }
}