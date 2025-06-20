# LLM Feedback Processor - Architecture Diagrams

## 🏗️ System Architecture Overview

```mermaid
graph TB
    subgraph "Frontend Layer"
        A[React Dashboard]
        B[Feedback Form]
        C[Live Feed Display]
    end
    
    subgraph "Backend Layer"
        D[Spring Boot App]
        E[Feedback Controller]
        F[Kafka Consumer]
        G[OpenAI Service]
        H[Redis Service]
        I[WebSocket Handler]
    end
    
    subgraph "Infrastructure Layer"
        J[Apache Kafka]
        K[Redis Cache]
        L[OpenAI API]
    end
    
    A --> E
    B --> E
    E --> J
    J --> F
    F --> G
    G --> L
    F --> H
    H --> K
    F --> I
    I --> C
    
    style A fill:#61dafb
    style D fill:#6db33f
    style J fill:#ff6b35
    style K fill:#dc382d
    style L fill:#10a37f
```

## 🔄 Complete Data Flow

```mermaid
sequenceDiagram
    participant U as User
    participant F as Frontend
    participant C as Controller
    participant K as Kafka
    participant CO as Consumer
    participant O as OpenAI
    participant R as Redis
    participant W as WebSocket
    
    U->>F: Submit Feedback
    F->>C: POST /api/feedback
    C->>K: Send to "feedback-input" topic
    C->>F: Return "Feedback submitted"
    F->>U: Show confirmation
    
    K->>CO: Consume feedback message
    CO->>O: Call GPT-3.5-turbo API
    O->>CO: Return enriched data
    
    CO->>R: Store enriched feedback
    CO->>W: Send via WebSocket
    W->>F: Push to /topic/feedback
    F->>U: Update dashboard in real-time
```

## 🏛️ Component Architecture

```mermaid
graph LR
    subgraph "Spring Boot Application"
        subgraph "Controllers"
            C1[FeedbackController]
            C2[HealthController]
        end
        
        subgraph "Services"
            S1[OpenAIService]
            S2[RedisService]
        end
        
        subgraph "Consumers"
            K1[FeedbackConsumer]
        end
        
        subgraph "Configuration"
            CF1[KafkaConfig]
            CF2[RedisConfig]
            CF3[WebSocketConfig]
        end
        
        subgraph "Models"
            M1[Feedback]
            M2[FeedbackEnriched]
        end
    end
    
    C1 --> K1
    K1 --> S1
    K1 --> S2
    C1 --> S2
    
    style C1 fill:#ff9999
    style S1 fill:#99ccff
    style K1 fill:#99ff99
```

## 📊 Data Models

```mermaid
classDiagram
    class Feedback {
        +String userId
        +String message
        +Long timestamp
        +getUserId()
        +getMessage()
        +getTimestamp()
    }
    
    class FeedbackEnriched {
        +String userId
        +String message
        +Long timestamp
        +String sentiment
        +String category
        +String summary
        +getUserId()
        +getMessage()
        +getTimestamp()
        +getSentiment()
        +getCategory()
        +getSummary()
    }
    
    Feedback --> FeedbackEnriched : enriched by AI
```

## 🚀 Deployment Architecture

```mermaid
graph TB
    subgraph "Docker Containers"
        subgraph "Application"
            A1[Spring Boot App]
            A2[React App]
        end
        
        subgraph "Infrastructure"
            I1[Kafka Broker]
            I2[Redis Server]
            I3[Zookeeper]
        end
    end
    
    subgraph "External Services"
        E1[OpenAI API]
    end
    
    A1 --> I1
    A1 --> I2
    A1 --> E1
    I1 --> I3
    
    style A1 fill:#6db33f
    style A2 fill:#61dafb
    style I1 fill:#ff6b35
    style I2 fill:#dc382d
    style E1 fill:#10a37f
```

## 🔧 Technology Stack

```mermaid
mindmap
  root((LLM Feedback Processor))
    Backend
      Spring Boot 3.2.0
        Web Starter
        Kafka Starter
        Redis Starter
        WebSocket Starter
        Actuator
        Validation
      Java 17
      Maven
      Lombok
    Frontend
      React 18.2.0
        Axios
        SockJS
        StompJS
        React Router
      Create React App
    Infrastructure
      Apache Kafka
      Redis
      Docker
      Docker Compose
    External APIs
      OpenAI GPT-3.5-turbo
```

## 📈 Processing Pipeline

```mermaid
flowchart LR
    A[User Input] --> B[Controller]
    B --> C[Kafka Topic]
    C --> D[Consumer]
    D --> E[OpenAI API]
    E --> F[Enrichment]
    F --> G[Redis Storage]
    F --> H[WebSocket Push]
    H --> I[Frontend Update]
    
    style A fill:#e1f5fe
    style E fill:#10a37f
    style G fill:#dc382d
    style I fill:#61dafb
```

## 🔄 Error Handling Flow

```mermaid
flowchart TD
    A[Process Feedback] --> B{OpenAI Available?}
    B -->|Yes| C[Call OpenAI API]
    B -->|No| D[Use Fallback]
    
    C --> E{API Success?}
    E -->|Yes| F[Store Enriched Data]
    E -->|No| D
    
    D --> G[Set Neutral Sentiment]
    G --> F
    
    F --> H{Redis Available?}
    H -->|Yes| I[Store in Redis]
    H -->|No| J[Store in Memory]
    
    I --> K[Send WebSocket]
    J --> K
    
    K --> L{WebSocket Success?}
    L -->|Yes| M[Update Frontend]
    L -->|No| N[Log Error]
    
    style D fill:#ffebee
    style J fill:#fff3e0
    style N fill:#ffebee
``` 