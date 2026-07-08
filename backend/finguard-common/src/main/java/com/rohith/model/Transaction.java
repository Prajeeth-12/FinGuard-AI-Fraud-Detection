package com.rohith.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    private String transactionId;
    private String cardNumber;
    private BigDecimal amount;
    private String merchantCity;
    private String merchantName;
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private Instant timestamp;

    private TransactionStatus status;
}
