package com.surendra.paymentdemo.consumer; // consumer package

import com.surendra.paymentdemo.event.PaymentEvent; // event object
import org.slf4j.Logger; // logger
import org.slf4j.LoggerFactory; // logger factory
import org.springframework.kafka.annotation.KafkaListener; // listens to kafka topic
import org.springframework.stereotype.Component; // spring bean

@Component // spring creates consumer object
public class PaymentEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventConsumer.class); // logger

    @KafkaListener(topics = "payment-events", groupId = "payment-consumer-group") // listens topic
    public void consume(PaymentEvent event) { // kafka gives message here

        log.info("payment event consumed transactionId={} status={}", event.transactionId(), event.status()); // consumer log

        // here real app can send notification, audit, settlement, reporting
    }
}