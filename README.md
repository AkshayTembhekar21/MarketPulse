
# MarketPulse
This repository is archived. The project has been split into:
- [MarketPulse-market-data](https://github.com/AkshayTembhekar21/MarketPulse-market-data)
- [MarketPulse-processor](https://github.com/AkshayTembhekar21/MarketPulse-processor)
- [MarketPulse-ui-desk](https://github.com/AkshayTembhekar21/MarketPulse-ui-desk)
- [MarketPulse-config](https://github.com/AkshayTembhekar21/MarketPulse-config)

MarketPulse is a real-time crypto market data processing system built with Spring Boot and Apache Kafka. It ingests market data via WebSocket, processes and stores trade information, and publishes updates to Kafka topics for downstream consumers.
Architechture
┌─────────────────────────────────────────────────────────────────────────────┐
│                           MARKETPULSE - LOCAL SETUP                          │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   FINNHUB API   │    │   KAFKA UI      │    │   H2 DATABASE   │
│   (External)    │    │   (Port 9000)   │    │   (In-Memory)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         │                       │                       │
         ▼                       │                       │
┌─────────────────┐              │                       │
│ MARKET-DATA     │              │                       │
│ SERVICE         │──────────────┼───────────────────────┼──┐
│ (Port 8080)     │              │                       │  │
│                 │              │                       │  │
│ • WebSocket     │              │                       │  │
│   Client        │              │                       │  │
│ • Kafka         │              │                       │  │
│   Producer      │              │                       │  │
└─────────────────┘              │                       │  │
         │                       │                       │  │
         │                       │                       │  │
         ▼                       │                       │  │
┌─────────────────┐              │                       │  │
│     KAFKA       │              │                       │  │
│   (Port 9092)   │              │                       │  │
│                 │              │                       │  │
│ • Zookeeper     │              │                       │  │
│   (Port 2181)   │              │                       │  │
└─────────────────┘              │                       │  │
         │                       │                       │  │
         │                       │                       │  │
         ▼                       │                       │  │
┌─────────────────┐              │                       │  │
│   PROCESSOR     │              │                       │  │
│   SERVICE       │──────────────┼───────────────────────┼──┘
│   (Port 8081)   │              │                       │
│                 │              │                       │
│ • Kafka         │              │                       │
│   Consumer      │              │                       │
│ • Trade         │              │                       │
│   Processing    │              │                       │
│ • WebSocket     │              │                       │
│   Publisher     │              │                       │
└─────────────────┘              │                       │
         │                       │                       │
         │                       │                       │
         ▼                       │                       │
┌─────────────────┐              │                       │
│   UI-DESK       │              │                       │
│   SERVICE       │──────────────┼───────────────────────┼──┐
│   (Port 8082)   │              │                       │  │
│                 │              │                       │  │
│ • Web UI        │              │                       │  │
│ • Kafka         │              │                       │  │
│   Consumer      │              │                       │  │
│ • WebSocket     │              │                       │  │
│   Handler       │              │                       │  │
└─────────────────┘              │                       │  │
         │                       │                       │  │
         ▼                       │                       │  │
┌─────────────────┐              │                       │  │
│   BROWSER       │              │                       │  │
│   (User)        │              │                       │  │
└─────────────────┘              │                       │  │
                                 │                       │  │
                                 ▼                       │  │
                        ┌─────────────────┐              │  │
                        │   DOCKER        │              │  │
                        │   COMPOSE       │              │  │
                        │   (Local)       │              │  │
                        └─────────────────┘              │  │
                                                         │  │
                                                         │  │
                                                         │  │
                                                         │  │
                                                         │  │
                                    
Now, let's explain this diagram in simple terms:

- **Finnhub API**: This is an external service providing real-time market data.
- **MarketPulse-market-data**: This service connects to Finnhub via WebSocket, receives market data, and pushes it to Kafka.
- **Kafka & Zookeeper**: Kafka is a message broker that allows different services to communicate asynchronously. Zookeeper helps manage Kafka.
- **MarketPulse-processor**: This service consumes market data from Kafka, processes it (e.g., trade logic), stores it in an H2 in-memory database, and can send updates via WebSocket.
- **MarketPulse-ui-desk**: This is the user interface service. It consumes updates from Kafka and/or WebSocket and serves a web UI to the user.
- **Browser**: The end user interacts with the UI via their browser.
- **Docker Compose**: Used locally to orchestrate (run) all these services together on your machine.

---

## 🏗️ **TARGET ARCHITECTURE: AWS EKS (Cloud Deployment)**

Here's how your project will look after we move it to AWS EKS:


# Prerequisites

Java 17+

Apache Kafka 3.7+: Download Kafka

Zookeeper (bundled with Kafka)

Git

Maven


# Kafka Installation & Setup
1. Install Java
Ensure Java is installed:
java -version
2. In the root dir run docker compose up -d to run the kafka image.
3. Create 'market-data' topic

   bin/kafka-topics.sh --create --topic market-data --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1

Create 'trade-updates' topic

    bin/kafka-topics.sh --create --topic trade-updates --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
If not created after running the app.

# Build and Run the Application
1. In every microservice open a terminal and run this command
 
    mvn spring-boot:run

Once all the 3 services are running hit http://localhost:8082 url. You'll see changing values of BTC in USD.
![image](https://github.com/user-attachments/assets/bafaee5f-80fe-4d72-aa6a-f61efc24426a)


