package com.surendra.paymentdemo.dto; // dto package

public record PaymentResponse( // response sent back to client

                               String transactionId, // unique id for this payment

                               String status, // SUCCESS or FAILED

                               String message // readable message for client

) { } // record makes immutable response object