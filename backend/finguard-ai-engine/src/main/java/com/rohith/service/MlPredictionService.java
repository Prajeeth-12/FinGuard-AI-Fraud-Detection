package com.rohith.service;

import com.rohith.dto.MlRequest;
import com.rohith.dto.MlResponse;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MlPredictionService {
    @Autowired
    private RestTemplate restTemplate;

    public double predict(MlRequest request)
    {
        String url="http://localhost:5000/predict";

        MlResponse response=restTemplate.postForObject(url,request,MlResponse.class);

        return response.getFraud_score();
    }
}
