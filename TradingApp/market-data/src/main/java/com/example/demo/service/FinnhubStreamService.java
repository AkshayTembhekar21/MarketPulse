package com.example.demo.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class FinnhubStreamService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private WebSocketClient webSocketClient;

    // Replace with your actual API key from Finnhub
    private final String apiKey = "d0remd9r01qn4tji8850d0remd9r01qn4tji885g";
    private final String socketUrl = "wss://ws.finnhub.io?token=" + apiKey;
    private final String topic = "market-data";

    @PostConstruct
    public void connect() {
        try {
            URI uri = new URI(socketUrl);

            webSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake handshake) {
                    System.out.println("✅ Connected to Finnhub WebSocket");

                    // Subscribe to USD trades
                    send("{\"type\":\"subscribe\",\"symbol\":\"BINANCE:BTCUSDT\"}");

                }

                @Override
                public void onMessage(String message) {
                    System.out.println("📩 Incoming message: " + message);
                    // Push to Kafka
                    kafkaTemplate.send(topic, message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("❌ WebSocket closed: Code=" + code + ", Reason=" + reason);
                }

                @Override
                public void onError(Exception ex) {
                    System.out.println("⚠️ WebSocket error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            };

            webSocketClient.connect();

        } catch (URISyntaxException e) {
            System.out.println("🚫 Invalid WebSocket URI");
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void disconnect() {
        if (webSocketClient != null && !webSocketClient.isClosed()) {
            System.out.println("🔌 Closing WebSocket connection...");
            webSocketClient.close();
        }
    }
}
