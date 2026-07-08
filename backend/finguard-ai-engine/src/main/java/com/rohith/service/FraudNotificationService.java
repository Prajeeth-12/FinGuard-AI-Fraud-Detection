package com.rohith.service;
import com.rohith.dto.RiskEvaluation;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FraudNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

//    "Send this RiskEvaluation message to everyone listening on /topic/fraud."

    public void sendFraudALert(RiskEvaluation evaluation)
    {
        System.out.println("IT came here");
        messagingTemplate.convertAndSend("/topic/fraud",evaluation);
    }
}
