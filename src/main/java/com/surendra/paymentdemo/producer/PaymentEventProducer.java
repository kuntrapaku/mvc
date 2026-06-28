package com.surendra.paymentdemo.producer; // producer package

import com.surendra.paymentdemo.event.PaymentEvent; // event object
import org.slf4j.Logger; // logger
import org.slf4j.LoggerFactory; // logger factory
import org.springframework.kafka.core.KafkaTemplate; // sends message to kafka
import org.springframework.stereotype.Component; // spring bean

@Component // spring creates this producer object
public class PaymentEventProducer {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventProducer.class); // logger

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate; // key is String, value is PaymentEvent

    public PaymentEventProducer(KafkaTemplate<String, PaymentEvent> kafkaTemplate) { // constructor injection
        this.kafkaTemplate = kafkaTemplate; // store kafka template
    }

    public void publish(PaymentEvent event) { // method to publish payment event

        kafkaTemplate.send("payment-events", event.transactionId(), event); // topic, key, message

        log.info("payment event sent transactionId={}", event.transactionId()); // production trace
    }
}