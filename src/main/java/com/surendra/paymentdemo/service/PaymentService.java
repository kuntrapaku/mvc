package com.surendra.paymentdemo.service; // service package has business logic

import com.surendra.paymentdemo.dto.PaymentRequest; // api request object
import com.surendra.paymentdemo.dto.PaymentResponse; // api response object
import com.surendra.paymentdemo.entity.Account; // account entity from db
import com.surendra.paymentdemo.entity.PaymentTransaction; // transaction history entity
import com.surendra.paymentdemo.event.PaymentEvent; // kafka event object
import com.surendra.paymentdemo.exception.AccountNotFoundException; // custom account error
import com.surendra.paymentdemo.producer.PaymentEventProducer; // kafka producer
import com.surendra.paymentdemo.repository.AccountRepository; // account db helper
import com.surendra.paymentdemo.repository.PaymentTransactionRepository; // transaction db helper
import org.slf4j.Logger; // logging interface
import org.slf4j.LoggerFactory; // creates logger object
import org.springframework.stereotype.Service; // tells spring this is service bean
import org.springframework.transaction.annotation.Transactional; // rollback if error happens

import java.util.UUID; // creates unique transaction id

@Service // spring creates object for this class
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class); // logger for this class

    private final AccountRepository accountRepository; // service needs account db operations

    private final PaymentTransactionRepository transactionRepository; // service saves payment history

    private final PaymentEventProducer paymentEventProducer; // service publishes kafka event

    public PaymentService(AccountRepository accountRepository,
                          PaymentTransactionRepository transactionRepository,
                          PaymentEventProducer paymentEventProducer) { // spring injects all dependencies

        this.accountRepository = accountRepository; // store account repository

        this.transactionRepository = transactionRepository; // store transaction repository

        this.paymentEventProducer = paymentEventProducer; // store kafka producer
    }

    @Transactional // debit, credit, history save should succeed together or rollback together
    public PaymentResponse transferMoney(PaymentRequest request) { // main money transfer method

        log.info("transfer started from={} to={} amount={}",
                request.fromAccountId(),
                request.toAccountId(),
                request.amount()); // log request start

        Account fromAccount = accountRepository.findById(request.fromAccountId()) // fetch sender from db
                .orElseThrow(() -> new AccountNotFoundException("sender account not found")); // stop if sender missing

        Account toAccount = accountRepository.findById(request.toAccountId()) // fetch receiver from db
                .orElseThrow(() -> new AccountNotFoundException("receiver account not found")); // stop if receiver missing

        fromAccount.debit(request.amount()); // reduce sender balance

        toAccount.credit(request.amount()); // increase receiver balance

        accountRepository.save(fromAccount); // save sender balance

        accountRepository.save(toAccount); // save receiver balance

        String transactionId = UUID.randomUUID().toString(); // create unique transaction id

        PaymentTransaction transaction = new PaymentTransaction(
                transactionId,
                fromAccount,
                toAccount,
                request.amount(),
                "SUCCESS"
        ); // create transaction history row

        transactionRepository.save(transaction); // save transaction history into db

        PaymentEvent event = new PaymentEvent(
                transactionId,
                fromAccount.getId(),
                toAccount.getId(),
                request.amount(),
                "SUCCESS"
        ); // create kafka event message

        paymentEventProducer.publish(event); // publish event to kafka

        log.info("transfer completed transactionId={}", transactionId); // success log

        return new PaymentResponse(transactionId, "SUCCESS", "money transferred successfully"); // api response
    }
}