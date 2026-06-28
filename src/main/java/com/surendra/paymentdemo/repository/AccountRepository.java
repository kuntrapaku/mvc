package com.surendra.paymentdemo.repository; // repository package

import com.surendra.paymentdemo.entity.Account; // using Account entity
import org.springframework.data.jpa.repository.JpaRepository; // gives ready db methods

public interface AccountRepository extends JpaRepository<Account, Long> {
    // Account = which entity/table
    // Long = type of primary key
    // spring now gives save(), findById(), findAll(), deleteById()
}