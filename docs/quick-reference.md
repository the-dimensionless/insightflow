# LLM Feedback Processor - Quick Reference

## 🚀 **Quick Start Flow**

```
User → Frontend → Backend → Kafka → AI → Redis → WebSocket → Frontend
```

## 📋 **Step-by-Step Process**

### **1. User Submits Feedback**
```
┌─────────────┐     POST /api/feedback     ┌─────────────┐
│   User      │ ──────────────────────────► │  Backend    │
│   (Browser) │                             │             │
└─────────────┘                             └─────────────┘
```

### **2. Backend Processes**
```
┌─────────────┐     Kafka Topic     ┌─────────────┐
│ Controller  │ ──────────────────► │  Consumer   │
│             │   "feedback-input"  │             │
└─────────────┘                     └─────────────┘
```

### **3. AI Enrichment**
```
┌─────────────┐     OpenAI API      ┌─────────────┐
│ Consumer    │ ──────────────────► │   OpenAI    │
│             │   GPT-3.5-turbo     │             │
└─────────────┘                     └─────────────┘
```

### **4. Storage & Push**
```
┌─────────────┐     Redis + WS      ┌─────────────┐
│ Consumer    │ ──────────────────► │  Frontend   │
│             │   Real-time         │             │
└─────────────┘                     └─────────────┘
```

## 🏗️ **System Components**

```
┌─────────────────────────────────────────────────────────────┐
│                    LLM Feedback Processor                    │
├─────────────────────────────────────────────────────────────┤
│  Frontend (React)  │  Backend (Spring Boot)  │  Infrastructure │
│                    │                         │                │
│  • Dashboard       │  • Controllers          │  • Kafka       │
│  • Forms           │  • Services             │  • Redis       │
│  • Live Feed       │  • Consumers            │  • Docker      │
│  • WebSocket       │  • WebSocket            │  • OpenAI API  │
└─────────────────────────────────────────────────────────────┘
```

## 🔧 **Key Commands**

### **Development**
```bash
# Start infrastructure
docker-compose up -d

# Start backend
./mvnw spring-boot:run

# Start frontend
npm start
```

### **Testing**
```bash
# Test backend
curl -X POST http://localhost:8080/api/feedback \
  -H "Content-Type: application/json" \
  -d '{"userId":"test","message":"Great app!"}'
```

## 📊 **Data Flow Summary**

```
Input:  {"userId": "user123", "message": "I love this app!"}
        ↓
Kafka:  Raw feedback message
        ↓
OpenAI: Sentiment: positive, Category: praise, Summary: "User loves the app"
        ↓
Redis:  Stored enriched data
        ↓
Output: Real-time dashboard update
```

## 🎯 **Key Features**

- ✅ **Real-time Processing** - Instant feedback analysis
- ✅ **AI Enrichment** - Sentiment, category, and summary
- ✅ **Scalable Architecture** - Kafka + Redis
- ✅ **Live Updates** - WebSocket push notifications
- ✅ **Docker Ready** - Easy deployment
- ✅ **Monitoring** - Health checks and metrics

## 🔗 **Important URLs**

- **Frontend:** http://localhost:3000
- **Backend API:** http://localhost:8080
- **Health Check:** http://localhost:8080/api/health
- **WebSocket:** ws://localhost:8080/ws-feedback

## 📝 **Environment Variables**

```bash
OPENAI_API_KEY=your-openai-api-key
SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092
SPRING_REDIS_HOST=localhost
SPRING_REDIS_PORT=6379
```

## 🚨 **Troubleshooting**

### **Common Issues**
1. **Java Version** - Ensure Java 17 is installed
2. **Maven** - Run `./mvnw clean install`
3. **Docker** - Ensure containers are running
4. **OpenAI API** - Check API key is valid

### **Logs**
```bash
# Backend logs
./mvnw spring-boot:run

# Docker logs
docker-compose logs -f
``` 