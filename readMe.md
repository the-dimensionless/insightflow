# 🚀 LLM Feedback Processor

A real-time feedback processing system that uses AI to analyze user feedback and provides instant insights through sentiment analysis, categorization, and summarization.

## 🏗️ **System Architecture**

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │    Backend      │    │  Infrastructure │
│   (React)       │    │  (Spring Boot)  │    │   (Docker)      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   User Input    │    │   API Gateway   │    │   Kafka + Redis │
│   Dashboard     │    │   WebSocket     │    │   OpenAI API    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🔄 **How It Works**

1. **User submits feedback** through the React dashboard
2. **Backend receives** the feedback via REST API
3. **Kafka processes** the message asynchronously
4. **OpenAI analyzes** the feedback using GPT-3.5-turbo
5. **Redis stores** the enriched data
6. **WebSocket pushes** real-time updates to the frontend
7. **Dashboard updates** automatically with AI insights

## 🎯 **Key Features**

- ✅ **Real-time Processing** - Instant feedback analysis
- ✅ **AI Enrichment** - Sentiment, category, and summary generation
- ✅ **Scalable Architecture** - Kafka message queuing + Redis caching
- ✅ **Live Updates** - WebSocket push notifications
- ✅ **Docker Ready** - Easy deployment and scaling
- ✅ **Monitoring** - Health checks and metrics

## 🛠️ **Technology Stack**

### **Backend**
- **Framework:** Spring Boot 3.2.0
- **Language:** Java 17
- **Build Tool:** Maven
- **Message Queue:** Apache Kafka
- **Cache:** Redis
- **Real-time:** WebSocket
- **AI:** OpenAI GPT-3.5-turbo

### **Frontend**
- **Framework:** React 18.2.0
- **HTTP Client:** Axios
- **WebSocket:** SockJS + StompJS
- **Routing:** React Router

### **Infrastructure**
- **Containerization:** Docker + Docker Compose
- **CI/CD:** GitHub Actions

## 🚀 **Quick Start**

### **Prerequisites**
- Java 17
- Node.js 16+
- Docker & Docker Compose
- OpenAI API Key

### **1. Clone the Repository**
```bash
git clone <repository-url>
cd insightflow
```

### **2. Set Environment Variables**
```bash
# Create .env file
echo "OPENAI_API_KEY=your-openai-api-key" > .env
```

### **3. Start Infrastructure**
```bash
docker-compose up -d
```

### **4. Start Backend**
```bash
cd backend
./mvnw spring-boot:run
```

### **5. Start Frontend**
```bash
cd frontend
npm install
npm start
```

### **6. Access the Application**
- **Frontend:** http://localhost:3000
- **Backend API:** http://localhost:8080
- **Health Check:** http://localhost:8080/api/health

## 📊 **API Endpoints**

### **Submit Feedback**
```bash
POST /api/feedback
Content-Type: application/json

{
  "userId": "user123",
  "message": "I love the new dashboard!"
}
```

### **Health Check**
```bash
GET /api/health
```

### **WebSocket Connection**
```
ws://localhost:8080/ws-feedback
Topic: /topic/feedback
```

## 🔧 **Configuration**

### **Environment Variables**
```bash
# OpenAI Configuration
OPENAI_API_KEY=your-openai-api-key

# Kafka Configuration
SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092

# Redis Configuration
SPRING_REDIS_HOST=localhost
SPRING_REDIS_PORT=6379

# Application Configuration
SERVER_PORT=8080
```

### **Docker Services**
- **Kafka:** localhost:9092
- **Redis:** localhost:6379
- **Zookeeper:** localhost:2181

## 📈 **Data Flow Example**

### **Input Feedback**
```json
{
  "userId": "user123",
  "message": "The new dashboard is amazing! Much better than before.",
  "timestamp": 1718900000
}
```

### **AI Enrichment Output**
```json
{
  "userId": "user123",
  "message": "The new dashboard is amazing! Much better than before.",
  "timestamp": 1718900000,
  "sentiment": "positive",
  "category": "praise",
  "summary": "User is very satisfied with the new dashboard improvements"
}
```

## 🏛️ **Project Structure**

```
insightflow/
├── backend/                 # Spring Boot Application
│   ├── src/main/java/
│   │   └── com/insightflow/
│   │       ├── controller/  # REST Controllers
│   │       ├── service/     # Business Logic
│   │       ├── config/      # Configuration
│   │       ├── model/       # Data Models
│   │       └── consumer/    # Kafka Consumers
│   ├── pom.xml
│   └── Dockerfile
├── frontend/                # React Application
│   ├── src/
│   │   ├── components/      # React Components
│   │   ├── services/        # API Services
│   │   └── utils/           # Utilities
│   ├── package.json
│   └── Dockerfile
├── docs/                    # Documentation
│   ├── system-flow.md       # Detailed system flow
│   ├── architecture-diagram.md # Mermaid diagrams
│   └── quick-reference.md   # Quick reference guide
├── docker-compose.yml       # Infrastructure setup
└── README.md               # This file
```

## 🔍 **Monitoring & Health Checks**

### **Spring Boot Actuator**
- **Health:** http://localhost:8080/actuator/health
- **Metrics:** http://localhost:8080/actuator/metrics
- **Info:** http://localhost:8080/actuator/info

### **Docker Health Checks**
```bash
# Check container status
docker-compose ps

# View logs
docker-compose logs -f kafka
docker-compose logs -f redis
```

## 🚨 **Troubleshooting**

### **Common Issues**

1. **Java Version Mismatch**
   ```bash
   # Check Java version
   java -version
   
   # Should show Java 17
   ```

2. **Maven Build Issues**
   ```bash
   cd backend
   ./mvnw clean install
   ```

3. **Docker Issues**
   ```bash
   # Restart containers
   docker-compose down
   docker-compose up -d
   ```

4. **OpenAI API Issues**
   - Verify API key is valid
   - Check API quota and limits
   - Ensure network connectivity

### **Logs**
```bash
# Backend logs
./mvnw spring-boot:run

# Docker logs
docker-compose logs -f

# Frontend logs
npm start
```

## 🧪 **Testing**

### **Backend Testing**
```bash
cd backend
./mvnw test
```

### **API Testing**
```bash
# Test feedback submission
curl -X POST http://localhost:8080/api/feedback \
  -H "Content-Type: application/json" \
  -d '{"userId":"test","message":"Great app!"}'

# Test health check
curl http://localhost:8080/api/health
```

## 📚 **Documentation**

- **[System Flow](docs/system-flow.md)** - Detailed system architecture and flow
- **[Architecture Diagrams](docs/architecture-diagram.md)** - Visual diagrams using Mermaid
- **[Quick Reference](docs/quick-reference.md)** - Quick start and troubleshooting guide

## 🤝 **Contributing**

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 **License**

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 **Support**

For support and questions:
- Create an issue in the repository
- Check the troubleshooting section
- Review the documentation

---

**Built with ❤️ using Spring Boot, React, Kafka, Redis, and OpenAI**
