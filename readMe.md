# LLM Feedback Processor 🚀

A real-time feedback processing system using Kafka, Redis, and OpenAI's LLMs — with a live dashboard built in React.

---

## 🔧 Tech Stack

### Backend (Java + Spring Boot)

* Kafka (Producer/Consumer)
* Redis (for caching processed feedback)
* OpenAI (for sentiment & category tagging)
* WebSocket (to stream live updates)

### Frontend (React)

* Live dashboard with WebSocket client
* Displays AI-enriched feedback

### CI/CD

* GitHub Actions
* Docker Build for both frontend & backend
* Deployable via Docker Compose or EC2

---

## 🧠 Features

* Accepts user feedback via REST API or UI
* Streams raw feedback to Kafka topic `feedback-input`
* Consumes feedback, sends to OpenAI for enrichment
* Saves LLM output in Redis for fast access
* Pushes updates to frontend via WebSocket
* Deployable with CI/CD pipeline via GitHub Actions

---

## 🛠️ Project Structure

```
llm-feedback-processor/
├── backend/
│   ├── model/Feedback.java
│   ├── controller/FeedbackController.java
│   ├── service/FeedbackService.java
│   ├── consumer/FeedbackConsumer.java
│   ├── config/KafkaConfig.java
│   └── config/WebSocketConfig.java
├── frontend/
│   ├── src/components/LiveFeed.js
│   └── src/App.js
├── .github/workflows/deploy.yml
└── README.md
```

---

## 📦 How to Run

### Prerequisites:

* Java 17+
* Node.js 18+
* Docker (for Kafka + Redis)
* OpenAI API key

### 1. Start Kafka + Redis

```bash
docker-compose up -d
```

### 2. Start Backend

```bash
cd backend
./mvnw spring-boot:run
```

### 3. Start Frontend

```bash
cd frontend
npm install
npm start
```

---

## 🔄 CI/CD Setup

Uses GitHub Actions to:

* Build backend with Maven
* Build frontend with Node.js
* Create Docker images
* Deploy to server or Docker Compose

`.github/workflows/deploy.yml` example:

```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
    - name: Build Backend
      run: mvn -f backend/pom.xml clean package
    - name: Build Frontend
      run: |
        cd frontend
        npm install
        npm run build
    - name: Docker Build & Push
      run: |
        docker build -t your-dockerhub/backend ./backend
        docker build -t your-dockerhub/frontend ./frontend
```

---

## 🔌 API Endpoint

`POST /api/feedback`

```json
{
  "userId": "user123",
  "message": "I love the new dashboard!",
  "timestamp": 1718900000
}
```

---

## 📊 Example LLM Output

```json
{
  "sentiment": "positive",
  "category": "praise",
  "summary": "User loves the new dashboard."
}
```

---

## 📍 Roadmap

* [x] Kafka + Redis Integration
* [x] OpenAI enrichment
* [x] Admin dashboard with filters & analytics
* [x] CI/CD with GitHub Actions
* [ ] User auth layer
* [ ] EC2 + Kubernetes deployment

---

## 🤝 Contributing

Feel free to fork and suggest improvements or ideas!

---

## 📄 License

MIT License
