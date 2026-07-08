package com.rohith.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    public WebSocketConfig()
    {
        System.out.println("******** Registering /fraud-alerts ********");
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config)
    {
        //react will subscribe here
        config.enableSimpleBroker("/topic");

        //Client sends messages using /app

        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry)
    {
        //This is where the client (React) connects.
        System.out.println("REGISTERING WEBSOCKET ENDPOINT");
        registry
                .addEndpoint("/fraud-alerts")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
