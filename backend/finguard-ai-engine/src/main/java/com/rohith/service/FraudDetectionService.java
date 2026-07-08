package com.rohith.service;

import com.rohith.dto.MlRequest;
import com.rohith.dto.RiskEvaluation;
import com.rohith.model.Transaction;
import com.rohith.model.TransactionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class FraudDetectionService {
    @Autowired
    private MlPredictionService mlPredictionService;


    private final Map<String, List<Transaction>> historyByCard = new ConcurrentHashMap<>();

    private static final BigDecimal HIGH_AMOUNT_THRESHOLD = new BigDecimal("50000");
    private static final long IMPOSSIBLE_TRAVEL_SECONDS = 120;
    private static final int VOLUME_THRESHOLD = 3; // transactions
    private static final long VOLUME_WINDOW_SECONDS = 60;

    public RiskEvaluation evaluate(Transaction tx) {

        List<Transaction> history =
                historyByCard.getOrDefault(tx.getCardNumber(), new ArrayList<>());

        List<String>reasons=new ArrayList<>();
        int riskScore = 0;

        long timeGap=9999;
        int cityChanged=0;

        // 1. HIGH AMOUNT RULE
        if (tx.getAmount().compareTo(HIGH_AMOUNT_THRESHOLD) > 0) {
            riskScore += 50;
            reasons.add("High amount");
            log.warn("High amount detected: {}", tx.getTransactionId());
        }

        // 2. IMPOSSIBLE TRAVEL RULE
        if (!history.isEmpty()) {
            Transaction last = history.get(history.size() - 1);

            if (last.getMerchantCity() != null &&
                    tx.getMerchantCity() != null &&
                    !last.getMerchantCity().equalsIgnoreCase(tx.getMerchantCity())) {

                timeGap = ChronoUnit.SECONDS.between(
                        last.getTimestamp(),
                        tx.getTimestamp()
                );

                if(!last.getMerchantCity().equalsIgnoreCase(tx.getMerchantCity()))
                {
                    cityChanged=1;
                    if (timeGap < IMPOSSIBLE_TRAVEL_SECONDS) {
                        riskScore += 40;
                        reasons.add("Impossible Travel");
                        log.warn("Impossible travel detected: {} -> {}",
                                last.getMerchantCity(), tx.getMerchantCity());
                    }
                }
            }
        }

        // 3. VOLUME / VELOCITY CHECK
        long recentCount = history.stream()
                .filter(t -> ChronoUnit.SECONDS.between(
                        t.getTimestamp(),
                        tx.getTimestamp()) <= VOLUME_WINDOW_SECONDS)
                .count();

        if (recentCount >= VOLUME_THRESHOLD) {
            riskScore += 30;
            reasons.add("High Transactions Velocity");
            log.warn("High velocity detected for card {}", tx.getCardNumber());
        }

        // 4. UPDATE HISTORY
        history.add(tx);
        historyByCard.put(tx.getCardNumber(), history);

        // keep memory clean (optional improvement)
        if (history.size() > 20) {
            history.remove(0);
        }


        MlRequest request=new MlRequest();
        request.setAmount(tx.getAmount().doubleValue());
        request.setTimeGapSeconds(timeGap);
        request.setCityChanged(cityChanged);
        request.setRecentTransactionCount((int)recentCount);

        double mlScore=mlPredictionService.predict(request);

        double finalScore=(riskScore * 0.4)+(mlScore *100 * 0.6);


        // 5. FINAL DECISION
        TransactionStatus status;
        if (finalScore >= 70) {
            log.warn("FRAUD - score={} txn={}", riskScore, tx.getTransactionId());
            status=TransactionStatus.FRAUD;

        } else if (finalScore >= 30) {
            log.warn("SUSPICIOUS - score={} txn={}", riskScore, tx.getTransactionId());
            status= TransactionStatus.SUSPICIOUS;
        } else {
            log.info("SAFE - score={} txn={}", riskScore, tx.getTransactionId());
            status= TransactionStatus.SAFE;
        }
        return RiskEvaluation.builder()
                .ruleScore(riskScore)
                .mlScore(mlScore)
                .finalScore(finalScore)
                .status(status)
                .reasons(reasons)
                .build();
    }
}