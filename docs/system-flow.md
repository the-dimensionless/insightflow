# LLM Feedback Processor - System Flow Documentation

## 🏗️ System Architecture Overview

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

## 🔄 **Complete Data Flow**

### **Step 1: User Submits Feedback**
```
┌─────────────┐     POST /api/feedback     ┌─────────────┐
│   Frontend  │ ──────────────────────────► │  Controller │
│   (React)   │                             │             │
└─────────────┘                             └─────────────┘
```

**What happens:**
- User fills out feedback form in React dashboard
- Frontend sends POST request to `/api/feedback`
- Request contains: `userId`, `message`, `timestamp`

### **Step 2: Controller Processes Request**
```
┌─────────────┐     Kafka Topic     ┌─────────────┐
│ Controller  │ ──────────────────► │   Kafka     │
│             │   "feedback-input"  │  Producer   │
└─────────────┘                     └─────────────┘
```

**What happens:**
- `FeedbackController` receives the feedback
- Sends feedback to Kafka topic `"feedback-input"`
- Returns "Feedback submitted." to frontend

### **Step 3: Kafka Consumer Processes Feedback**
```
┌─────────────┐     Kafka Topic     ┌─────────────┐
│   Kafka     │ ──────────────────► │  Consumer   │
│  Consumer   │   "feedback-input"  │             │
└─────────────┘                     └─────────────┘
```

**What happens:**
- `FeedbackConsumer` listens to `"feedback-input"` topic
- Receives raw feedback data
- Triggers AI enrichment process

### **Step 4: OpenAI AI Enrichment**
```
┌─────────────┐     OpenAI API      ┌─────────────┐
│  Consumer   │ ──────────────────► │  OpenAI     │
│             │   GPT-3.5-turbo     │   Service   │
└─────────────┘                     └─────────────┘
```

**What happens:**
- `OpenAIService` creates prompt from feedback message
- Calls OpenAI GPT-3.5-turbo API
- Receives JSON response with:
  - `sentiment` (positive/negative/neutral)
  - `category` (praise/complaint/suggestion/question/general)
  - `summary` (AI-generated summary)

### **Step 5: Store Enriched Data**
```
┌─────────────┐     Redis Storage   ┌─────────────┐
│  Consumer   │ ──────────────────► │    Redis    │
│             │   Key: feedback:    │             │
│             │   {userId}          │             │
└─────────────┘                     └─────────────┘
```

**What happens:**
- Enriched feedback stored in Redis
- Key format: `feedback:{userId}`
- Data includes original + AI analysis

### **Step 6: Real-time Push to Frontend**
```
┌─────────────┐     WebSocket       ┌─────────────┐
│  Consumer   │ ──────────────────► │  Frontend   │
│             │   /topic/feedback   │   (React)   │
└─────────────┘                     └─────────────┘
```

**What happens:**
- `SimpMessagingTemplate` sends enriched data
- WebSocket topic: `/topic/feedback`
- Frontend receives real-time updates
- Dashboard updates automatically

## 🏛️ **Component Architecture**

### **Backend Components**
```
┌─────────────────────────────────────────────────────────────┐
│                    Spring Boot Application                   │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│  │ Controller  │  │  Consumer   │  │   Service   │         │
│  │             │  │             │  │             │         │
│  │ • REST API  │  │ • Kafka     │  │ • OpenAI    │         │
│  │ • WebSocket │  │ • Redis     │  │ • Business  │         │
│  │             │  │ • WebSocket │  │   Logic     │         │
│  └─────────────┘  └─────────────┘  └─────────────┘         │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│  │   Config    │  │   Models    │  │   Utils     │         │
│  │             │  │             │  │             │         │
│  │ • Kafka     │  │ • Feedback  │  │ • Helpers   │         │
│  │ • Redis     │  │ • Enriched  │  │ • Constants │         │
│  │ • WebSocket │  │             │  │             │         │
│  └─────────────┘  └─────────────┘  └─────────────┘         │
└─────────────────────────────────────────────────────────────┘
```

### **Frontend Components**
```
┌─────────────────────────────────────────────────────────────┐
│                    React Application                        │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│  │    App      │  │  Components │  │   Services  │         │
│  │             │  │             │  │             │         │
│  │ • Main App  │  │ • LiveFeed  │  │ • API Calls │         │
│  │ • Routing   │  │ • Forms     │  │ • WebSocket │         │
│  │ • State     │  │ • Stats     │  │ • Utils     │         │
│  └─────────────┘  └─────────────┘  └─────────────┘         │
└─────────────────────────────────────────────────────────────┘
```

## 🔧 **Technology Stack**

### **Backend (Java + Spring Boot)**
- **Framework:** Spring Boot 3.2.0
- **Java Version:** 17
- **Build Tool:** Maven
- **Dependencies:**
  - Spring Web (REST API)
  - Spring Kafka (Message Queue)
  - Spring Data Redis (Caching)
  - Spring WebSocket (Real-time)
  - Spring Boot Actuator (Monitoring)
  - Lombok (Code Generation)
  - OpenAI API Client

### **Frontend (React)**
- **Framework:** React 18.2.0
- **Build Tool:** Create React App
- **Dependencies:**
  - Axios (HTTP Client)
  - SockJS + StompJS (WebSocket)
  - React Router (Navigation)

### **Infrastructure (Docker)**
- **Message Queue:** Apache Kafka
- **Cache/Storage:** Redis
- **Orchestration:** Docker Compose
- **CI/CD:** GitHub Actions

## 📈 **Data Models**

### **Feedback (Input)**
```json
{
  "userId": "user123",
  "message": "I love the new dashboard!",
  "timestamp": 1718900000
}
```

### **FeedbackEnriched (Output)**
```json
{
  "userId": "user123",
  "message": "I love the new dashboard!",
  "timestamp": 1718900000,
  "sentiment": "positive",
  "category": "praise",
  "summary": "User loves the new dashboard"
}
```

## 🚀 **Deployment Flow**

### **Development**
1. Start infrastructure: `docker-compose up -d`
2. Start backend: `./mvnw spring-boot:run`
3. Start frontend: `npm start`

### **Production**
1. Build Docker images
2. Deploy with Docker Compose
3. Configure environment variables
4. Set up monitoring and logging

## 🔍 **Key Features**

### **Real-time Processing**
- Instant feedback submission
- AI analysis within seconds
- Live dashboard updates

### **Scalability**
- Kafka for message queuing
- Redis for fast caching
- Microservices-ready architecture

### **AI Integration**
- OpenAI GPT-3.5-turbo
- Sentiment analysis
- Category classification
- Summary generation

### **Monitoring**
- Spring Boot Actuator
- Health checks
- Metrics collection
- Logging

## 🛠️ **Configuration**

### **Environment Variables**
```bash
OPENAI_API_KEY=your-openai-api-key
SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092
SPRING_REDIS_HOST=localhost
SPRING_REDIS_PORT=6379
```

### **API Endpoints**
- `POST /api/feedback` - Submit feedback
- `GET /api/health` - Health check
- `WS /ws-feedback` - WebSocket connection

### **Kafka Topics**
- `feedback-input` - Raw feedback messages

### **Redis Keys**
- `feedback:{userId}` - Enriched feedback data

## 🔄 **Error Handling**

### **OpenAI Failures**
- Fallback to neutral sentiment
- Error logging
- Graceful degradation

### **Kafka Failures**
- Retry mechanisms
- Dead letter queues
- Circuit breakers

### **Redis Failures**
- In-memory fallback
- Data persistence
- Recovery mechanisms 