package com.surendra.paymentdemo.controller; // controller package has api endpoints

import com.surendra.paymentdemo.dto.PaymentRequest; // request body from client
import com.surendra.paymentdemo.dto.PaymentResponse; // response body to client
import com.surendra.paymentdemo.service.PaymentService; // controller calls service
import org.springframework.http.ResponseEntity; // gives response with http status
import org.springframework.web.bind.annotation.PostMapping; // handles post api
import org.springframework.web.bind.annotation.RequestBody; // converts json to java object
import org.springframework.web.bind.annotation.RequestMapping; // base url
import org.springframework.web.bind.annotation.RestController; // makes this class rest api
import jakarta.validation.Valid; // tells spring to validate request body

@RestController // tells spring this class exposes rest api
@RequestMapping("/api/v1/payments") // common url for all payment apis
public class PaymentController {

    private final PaymentService paymentService; // controller needs service, not repository

    public PaymentController(PaymentService paymentService) { // constructor injection
        this.paymentService = paymentService; // spring injects service object here
    }

    @PostMapping("/transfer") // payment transfer api
    public ResponseEntity<PaymentResponse> transferMoney(@Valid @RequestBody PaymentRequest request) {
        // @Valid runs validations written in PaymentRequest

        PaymentResponse response = paymentService.transferMoney(request); // call service

        return ResponseEntity.ok(response); // return success response
    }
}