
# MarketPulse
This repository is archived. The project has been split into:
- [MarketPulse-market-data](https://github.com/AkshayTembhekar21/MarketPulse-market-data)
- [MarketPulse-processor](https://github.com/AkshayTembhekar21/MarketPulse-processor)
- [MarketPulse-ui-desk](https://github.com/AkshayTembhekar21/MarketPulse-ui-desk)
- [MarketPulse-config](https://github.com/AkshayTembhekar21/MarketPulse-config)

MarketPulse is a real-time crypto market data processing system built with Spring Boot and Apache Kafka. It ingests market data via WebSocket, processes and stores trade information, and publishes updates to Kafka topics for downstream consumers.

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


