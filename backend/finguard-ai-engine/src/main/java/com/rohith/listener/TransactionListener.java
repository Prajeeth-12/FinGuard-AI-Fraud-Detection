package com.rohith.listener;

import com.rohith.dto.RiskEvaluation;
import com.rohith.model.Transaction;
import com.rohith.model.TransactionStatus;
import com.rohith.service.FraudDetectionService;
import com.rohith.service.FraudNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.awt.image.renderable.RenderableImage;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionListener {
    private final FraudDetectionService fraudDetectionService;
    private final KafkaTemplate<String, RiskEvaluation>kafkaTemplate;
    private final FraudNotificationService fraudNotificationService;

    private static final String RESULT_TOPIC="transaction-results";

    @KafkaListener(topics = "transactions",groupId = "ai-engine-group")
    public void consume(Transaction transaction)
    {
        log.info("Consumed transaction: {}",transaction.getTransactionId());
        RiskEvaluation evaluation= fraudDetectionService.evaluate(transaction);

        kafkaTemplate.send(RESULT_TOPIC,transaction.getTransactionId(),evaluation);

        fraudNotificationService.sendFraudALert(evaluation);

        log.info("Published result [{}] for transaction: {}", evaluation, transaction.getTransactionId());
    }
}
