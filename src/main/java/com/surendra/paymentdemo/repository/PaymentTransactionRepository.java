package com.surendra.paymentdemo.repository; // repository package

import com.surendra.paymentdemo.entity.PaymentTransaction; // transaction entity
import org.springframework.data.jpa.repository.JpaRepository; // ready db methods

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, String> {
    // PaymentTransaction = entity
    // String = transactionId datatype
}