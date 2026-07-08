# 💳 FinGuard - Real-Time Fraud Detection System

A real-time credit card fraud detection system built using **Spring Boot Microservices, Apache Kafka, Machine Learning (Python), WebSocket, and HTML/React Dashboard**.

The system ingests card transactions, evaluates them using rule-based detection and a machine learning model, and pushes fraud alerts to a live dashboard.

---

# 🚀 Features

- Real-time transaction processing
- Rule-based fraud detection
- Machine Learning fraud scoring (Random Forest)
- Kafka event-driven architecture
- Spring Boot Microservices
- Live dashboard using WebSocket
- Fraud Risk Score (0 - 100)
- Transaction status
  - SAFE
  - SUSPICIOUS
  - FRAUD
- Detailed fraud reasons
- REST APIs
- Responsive frontend dashboard

---

# 🏗️ Architecture

```
                Card Swipe

                     │
                     ▼

        Ingestion Service (Spring Boot)

                     │
               Kafka Topic

                     │
                     ▼

        AI Engine (Spring Boot)

                     │
        ┌────────────┴────────────┐
        │                         │
        ▼                         ▼

 Rule Engine             ML Service (Python)

        │                         │
        └────────────┬────────────┘

                     ▼

          Risk Evaluation Result

                     │

           WebSocket Notification

                     │

          Frontend Dashboard
```

---

# 🛠️ Tech Stack

## Backend

- Java 17
- Spring Boot
- Spring MVC
- Apache Kafka
- WebSocket (STOMP + SockJS)
- Maven
- Lombok

## Machine Learning

- Python
- Flask
- Scikit-Learn
- Random Forest
- Pandas
- NumPy
- Joblib

## Frontend

- HTML
- CSS
- JavaScript

*(Can be upgraded to React in future.)*

---

# 📂 Project Structure

```
FinGuard/
│
├── backend/
│
│   ├── finguard-common/
│   ├── finguard-ingestion-service/
│   ├── finguard-ai-engine/
│
├── ml-service/
│
│   ├── app.py
│   ├── train_model.py
│   ├── model.pkl
│
├── frontend/
│
│   ├── dashboard.html
│   ├── payment.html
│   ├── css/
│   ├── js/
│
├── screenshots/
│
└── README.md
```

---

# ⚙️ Microservices

## 1. Ingestion Service

Responsible for

- Receiving card swipe requests
- Validating request
- Publishing transaction to Kafka

Endpoint

```
POST /api/transactions/swipe
```

---

## 2. AI Engine

Responsible for

- Consuming Kafka events
- Rule-based fraud detection
- Calling ML model
- Calculating final fraud score
- Publishing WebSocket alerts

---

## 3. ML Service

Python Flask application

Responsibilities

- Load trained Random Forest model
- Predict fraud probability
- Return ML score

Endpoint

```
POST /predict
```

---

# 📊 Fraud Detection Rules

Current implemented rules

### High Amount

Large transactions increase risk.

### Impossible Travel

Two transactions from different cities within a very short time.

### High Velocity

Multiple transactions within a small time window.

### Machine Learning Score

Random Forest model predicts fraud probability.

---

# 📈 Risk Calculation

```
Final Score =
Rule Score
+
Machine Learning Score
```

Example

```
Rule Score : 45

ML Score : 0.82

Final Score : 86

Status : FRAUD
```

---

# 📡 Kafka Topics

Transaction Topic

```
transactions
```

---

# 🔌 WebSocket

Endpoint

```
/fraud-alerts
```

Topic

```
/topic/fraud
```

Whenever a transaction is processed, the AI Engine pushes the result to all connected dashboard clients.

---

# 📦 REST APIs

## Submit Transaction

```
POST /api/transactions/swipe
```

Example

```json
{
  "cardNumber": "1234567812345678",
  "amount": 25000,
  "merchantName": "Amazon",
  "merchantCity": "Chennai"
}
```

---

## ML Prediction

```
POST /predict
```

# ▶️ Running the Project

## Clone

```bash
git clone https://github.com/yourusername/FinGuard.git
```

---

## Start Kafka

```bash
docker-compose up
```

---

## Run ML Service

```bash
cd ml-service

python train_model.py

python app.py
```

---

## Run Backend

Run

- finguard-common
- finguard-ingestion-service
- finguard-ai-engine

from your IDE.

---

## Open Frontend

Simply open

```
dashboard.html
```

or

```
payment.html
```

---

# 📷 Screenshots

Add screenshots here

- Dashboard
- Payment Page
- Fraud Alert
- Architecture Diagram

---

# 🚀 Future Improvements

- React Dashboard
- JWT Authentication
- Redis Caching
- Docker Compose
- Kubernetes Deployment
- Grafana Monitoring
- Prometheus Metrics
- Email Notifications
- SMS Alerts
- Admin Portal

---

# 👨‍💻 Author

**Rohith Kumar S**

GitHub: https://github.com/yourusername

---

# ⭐ If you found this project useful, please consider giving it a star.
