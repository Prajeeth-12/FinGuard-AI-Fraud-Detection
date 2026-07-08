package com.rohith.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MlRequest {
    private double amount;

    private double timeGapSeconds;

    private int cityChanged;

    private int recentTransactionCount;


}
