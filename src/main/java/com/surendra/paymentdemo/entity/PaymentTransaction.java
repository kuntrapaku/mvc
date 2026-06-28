package com.surendra.paymentdemo.entity; // entity package

import jakarta.persistence.*; // jpa annotations
import java.math.BigDecimal; // money type
import java.time.LocalDateTime; // transaction time

@Entity // save transaction into db
@Table(name = "payment_transactions") // table name
public class PaymentTransaction {

    @Id // primary key
    private String transactionId; // unique payment id

    @ManyToOne(fetch = FetchType.LAZY) // many transactions can belong to one sender account
    @JoinColumn(name = "from_account_id") // db column storing sender account id
    private Account fromAccount; // sender account object

    @ManyToOne(fetch = FetchType.LAZY) // many transactions can belong to one receiver account
    @JoinColumn(name = "to_account_id") // db column storing receiver account id
    private Account toAccount; // receiver account object

    private BigDecimal amount; // transferred amount

    private String status; // SUCCESS or FAILED

    private LocalDateTime createdAt; // when transaction happened

    protected PaymentTransaction() { } // hibernate needs empty constructor

    public PaymentTransaction(String transactionId, Account fromAccount, Account toAccount, BigDecimal amount, String status) {
        this.transactionId = transactionId; // assign transaction id
        this.fromAccount = fromAccount; // assign sender
        this.toAccount = toAccount; // assign receiver
        this.amount = amount; // assign amount
        this.status = status; // assign status
        this.createdAt = LocalDateTime.now(); // set current time
    }

    public String getTransactionId() { return transactionId; } // return transaction id

    public BigDecimal getAmount() { return amount; } // return amount

    public String getStatus() { return status; } // return status

    public LocalDateTime getCreatedAt() { return createdAt; } // return created time
}