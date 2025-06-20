import React, { useState, useEffect } from 'react';
import axios from 'axios';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import './App.css';

function App() {
  const [feedbacks, setFeedbacks] = useState([]);
  const [newFeedback, setNewFeedback] = useState({ userId: '', message: '' });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [stats, setStats] = useState({
    total: 0,
    positive: 0,
    negative: 0,
    neutral: 0
  });

  useEffect(() => {
    loadFeedbacks();
    setupWebSocket();
  }, []);

  const loadFeedbacks = async () => {
    try {
      const response = await axios.get('/api/feedback');
      setFeedbacks(response.data);
      updateStats(response.data);
      setLoading(false);
    } catch (err) {
      setError('Failed to load feedbacks');
      setLoading(false);
    }
  };

  const setupWebSocket = () => {
    const socket = new SockJS('http://localhost:8080/ws');
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, (frame) => {
      console.log('Connected to WebSocket');
      
      stompClient.subscribe('/topic/feedback', (message) => {
        const feedback = JSON.parse(message.body);
        setFeedbacks(prev => [feedback, ...prev]);
        updateStats([feedback, ...feedbacks]);
      });
    }, (error) => {
      console.error('WebSocket connection failed:', error);
    });
  };

  const updateStats = (feedbackList) => {
    const total = feedbackList.length;
    const positive = feedbackList.filter(f => f.sentiment === 'positive').length;
    const negative = feedbackList.filter(f => f.sentiment === 'negative').length;
    const neutral = feedbackList.filter(f => f.sentiment === 'neutral').length;
    
    setStats({ total, positive, negative, neutral });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!newFeedback.userId || !newFeedback.message) return;

    try {
      const feedback = {
        ...newFeedback,
        timestamp: Date.now()
      };

      await axios.post('/api/feedback', feedback);
      setNewFeedback({ userId: '', message: '' });
    } catch (err) {
      setError('Failed to submit feedback');
    }
  };

  const getSentimentClass = (sentiment) => {
    switch (sentiment) {
      case 'positive': return 'sentiment-positive';
      case 'negative': return 'sentiment-negative';
      default: return 'sentiment-neutral';
    }
  };

  if (loading) {
    return (
      <div className="container">
        <div className="loading">Loading feedbacks...</div>
      </div>
    );
  }

  return (
    <div className="container">
      <div className="card">
        <h1>🚀 LLM Feedback Processor</h1>
        <p>Real-time feedback processing with AI enrichment</p>
      </div>

      {/* Stats Cards */}
      <div className="grid">
        <div className="card stats-card">
          <div className="stats-number">{stats.total}</div>
          <div className="stats-label">Total Feedback</div>
        </div>
        <div className="card stats-card">
          <div className="stats-number">{stats.positive}</div>
          <div className="stats-label">Positive</div>
        </div>
        <div className="card stats-card">
          <div className="stats-number">{stats.negative}</div>
          <div className="stats-label">Negative</div>
        </div>
        <div className="card stats-card">
          <div className="stats-number">{stats.neutral}</div>
          <div className="stats-label">Neutral</div>
        </div>
      </div>

      {/* Submit Feedback Form */}
      <div className="card">
        <h2>Submit New Feedback</h2>
        <form onSubmit={handleSubmit}>
          <div style={{ marginBottom: '16px' }}>
            <input
              type="text"
              className="input"
              placeholder="User ID"
              value={newFeedback.userId}
              onChange={(e) => setNewFeedback({...newFeedback, userId: e.target.value})}
            />
          </div>
          <div style={{ marginBottom: '16px' }}>
            <textarea
              className="input"
              placeholder="Your feedback message..."
              rows="4"
              value={newFeedback.message}
              onChange={(e) => setNewFeedback({...newFeedback, message: e.target.value})}
            />
          </div>
          <button type="submit" className="btn">
            Submit Feedback
          </button>
        </form>
      </div>

      {/* Error Display */}
      {error && (
        <div className="error">
          {error}
        </div>
      )}

      {/* Live Feed */}
      <div className="card">
        <h2>📊 Live Feedback Feed</h2>
        <div>
          {feedbacks.map((feedback) => (
            <div key={feedback.id} className={`feedback-item ${getSentimentClass(feedback.sentiment)}`}>
              <div style={{ marginBottom: '8px' }}>
                <strong>User:</strong> {feedback.userId}
                <span style={{ marginLeft: '16px', color: '#666' }}>
                  {new Date(feedback.timestamp).toLocaleString()}
                </span>
              </div>
              <div style={{ marginBottom: '8px' }}>
                <strong>Message:</strong> {feedback.message}
              </div>
              {feedback.sentiment && (
                <div style={{ marginBottom: '4px' }}>
                  <span className="badge badge-sentiment">
                    {feedback.sentiment}
                  </span>
                  {feedback.category && (
                    <span className="badge badge-category">
                      {feedback.category}
                    </span>
                  )}
                </div>
              )}
              {feedback.summary && (
                <div style={{ fontSize: '14px', color: '#666', fontStyle: 'italic' }}>
                  <strong>AI Summary:</strong> {feedback.summary}
                </div>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default App; 