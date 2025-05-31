package com.server.processor.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.processor.model.Trade;
import com.server.processor.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    private double lastPrice = 0.0;

    public void processMessage(String message) {
        try {
            JsonNode jsonNode = mapper.readTree(message);

            if (jsonNode.has("data")) {
                for (JsonNode node : jsonNode.get("data")) {
                    String ticker = node.get("s").asText();
                    double price = node.get("p").asDouble();
                    long ts = node.get("t").asLong();

                    // Simple threshold logic
                    if (lastPrice == 0.0 || Math.abs(price - lastPrice) >= 0.5) {
                        lastPrice = price;

                        Trade trade = new Trade();
                        trade.setTicker(ticker);
                        trade.setPrice(price);
                        trade.setTimestamp(LocalDateTime.ofInstant(Instant.ofEpochMilli(ts), ZoneOffset.UTC));

                        tradeRepository.save(trade);

                        System.out.println("✅ Saved trade to DB: " + trade);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
// This service processes incoming market data messages, extracts trade information