package com.surendra.paymentdemo.event; // event package

import java.math.BigDecimal; // money type

public record PaymentEvent( // kafka message object

                            String transactionId, // unique payment id

                            Long fromAccountId, // sender account id

                            Long toAccountId, // receiver account id

                            BigDecimal amount, // transfer amount

                            String status // SUCCESS / FAILED

) { }