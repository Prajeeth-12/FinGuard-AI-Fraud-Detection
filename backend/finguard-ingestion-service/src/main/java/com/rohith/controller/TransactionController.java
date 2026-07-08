package com.rohith.controller;

import com.rohith.dto.SwipeRequest;
import com.rohith.model.Transaction;
import com.rohith.model.TransactionStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
@CrossOrigin(origins = {
        "http://127.0.0.1:5500",
        "http://localhost:5500"
})
public class TransactionController {
    private final KafkaTemplate<String,Transaction>kafkaTemplate;
    @PostMapping("/swipe")
    @ResponseStatus(HttpStatus.ACCEPTED)

    public TransactionsAckResponse ingestSwipe(@Valid @RequestBody SwipeRequest request)
    {
        Transaction transaction = Transaction.builder()
                .transactionId(UUID.randomUUID().toString())
                .cardNumber(request.getCardNumber())
                .amount(request.getAmount())
                .merchantCity(request.getMerchantCity())
                .merchantName(request.getMerchantName())
                .timestamp(Instant.now())
                .status(TransactionStatus.PENDING)
                .build();
            kafkaTemplate.send("transactions",transaction.getTransactionId(),transaction);
            log.info("Transcation received:{}",transaction);
            return new TransactionsAckResponse(
                    transaction.getTransactionId(),
                    "RECEIVED",
                    "Transcation is being procesed"
            );
    }
}

//Why Mono<SwipeRequest> as the parameter type instead of just SwipeRequest?
//This is the key WebFlux concept. With regular Spring MVC:
//javapublic ResponseEntity<X> ingestSwipe(@RequestBody SwipeRequest request)
//The thread blocks while Spring reads and parses the request body.
//With WebFlux:
//javapublic Mono<X> ingestSwipe(@RequestBody Mono<SwipeRequest> requestMono)
//The thread is released back to the pool while the body streams in, and your .map() logic only runs once data arrives — asynchronously. Multiply this by thousands of concurrent swipes, and this is the difference between a handful of threads serving everyone vs. one thread per connection.
