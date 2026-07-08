package com.rohith.dto;

import com.rohith.model.TransactionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
public class RiskEvaluation {

    private int ruleScore;
    private double mlScore;
    private double finalScore;

    private TransactionStatus status;
    private List<String> reasons;
}
