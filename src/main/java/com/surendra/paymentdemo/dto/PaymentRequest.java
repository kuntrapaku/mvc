package com.surendra.paymentdemo.dto; // dto package

import jakarta.validation.constraints.NotNull; // field must not be null
import jakarta.validation.constraints.Positive; // number must be greater than zero
import java.math.BigDecimal; // money type

public record PaymentRequest( // request from client

                              @NotNull(message = "from account id is required") // sender id mandatory
                              Long fromAccountId,

                              @NotNull(message = "to account id is required") // receiver id mandatory
                              Long toAccountId,

                              @NotNull(message = "amount is required") // amount mandatory
                              @Positive(message = "amount must be positive") // amount should be > 0
                              BigDecimal amount

) { }