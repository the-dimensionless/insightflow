# OpenAI + Redis Integration Setup

## Prerequisites
- Java 17+
- Maven
- Docker (for Kafka + Redis)
- OpenAI API key

## Setup Steps

### 1. Set OpenAI API Key
Update `backend/src/main/resources/application.yml`:
```yaml
openai:
  api-key: YOUR_ACTUAL_OPENAI_API_KEY_HERE
```

### 2. Start Infrastructure
```bash
docker-compose up -d
```

### 3. Start Backend
```bash
cd backend
./mvnw spring-boot:run
```

### 4. Test the Pipeline

#### Submit Feedback via API:
```bash
curl -X POST http://localhost:8080/api/feedback \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "message": "I love the new dashboard!",
    "timestamp": 1718900000
  }'
```

#### Check Redis Storage:
```bash
docker exec -it insightflow-redis-1 redis-cli
> KEYS *
> GET feedback:user123
```

#### Monitor WebSocket Messages:
The enriched feedback will be pushed to `/topic/feedback` via WebSocket.

## Architecture Flow

1. **Feedback Submission** → `POST /api/feedback`
2. **Kafka Producer** → Sends to `feedback-input` topic
3. **Kafka Consumer** → Listens to `feedback-input` topic
4. **OpenAI Enrichment** → Calls GPT-3.5-turbo for sentiment/category/summary
5. **Redis Storage** → Stores enriched feedback with key `feedback:{userId}`
6. **WebSocket Push** → Sends enriched data to frontend via `/topic/feedback`

## Expected Output

The enriched feedback will contain:
- `userId`: Original user ID
- `message`: Original message
- `timestamp`: Original timestamp
- `sentiment`: AI-detected sentiment (positive/negative/neutral)
- `category`: AI-detected category (praise/complaint/suggestion/question/general)
- `summary`: AI-generated summary

## Troubleshooting

### OpenAI API Issues
- Verify API key is correct
- Check API quota/credits
- Ensure network connectivity

### Redis Connection Issues
- Verify Redis is running: `docker ps`
- Check Redis logs: `docker logs insightflow-redis-1`

### Kafka Connection Issues
- Verify Kafka is running: `docker ps`
- Check Kafka logs: `docker logs insightflow-kafka-1` 