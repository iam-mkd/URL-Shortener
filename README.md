# 🚀 URL Shortener Service (Spring Boot + MongoDB + Redis)

A production-style, high-performance URL Shortener built with:

- Spring Boot 3
- Java 17
- MongoDB
- Redis
- Snowflake-style ID generation

Designed with scalability, performance, and clean architecture in mind.

## ✨ Features

- ⚡ Snowflake-based distributed ID generation
- 🔤 Base62 short code encoding
- 🗄 MongoDB for persistence
- ⏳ TTL index for auto-expiring URLs
- 🚀 Redis cache (cache-first redirect strategy)
- 📊 Click analytics stored in MongoDB
- 🎯 Custom short URL aliases
- 🚦 Redis-based rate limiting
- 📈 Click statistics endpoint
- 🧱 Clean layered architecture

## 🏗 High-Level Architecture

```
         ┌──────────────┐
         │    Client    │
         │ (Browser/API)│
         └──────┬───────┘
                │
                ▼
        ┌─────────────────┐
        │  Spring Boot    │
        │  URL Service    │
        └──────┬────┬─────┘
               │    │
               │    │
         Cache │    │ Persistent Storage
               │    │
               ▼    ▼
         ┌────────┐  ┌──────────────┐
         │ Redis  │  │  MongoDB     │
         │ Cache  │  │ (Documents)  │
         └────────┘  └──────────────┘
```

## Redirect Flow

```
Client → Spring Boot → Redis (hit?) → Redirect
                         │
                         └─ Miss → MongoDB → Cache → Redirect
```

## 🧠 ID & Code Generation Strategy

- Uses a Snowflake-style 64-bit ID generator
- Encodes numeric ID into Base62 string
- Produces short, URL-safe codes

**Example:**

| Snowflake ID | Base62 Code | Short URL |
|--------------|-------------|-----------|
| 72384723984723 | k9Xa2B | http://localhost:8080/k9Xa2B |

**Benefits:**

- No collisions
- Distributed-friendly
- High throughput
- Predictable performance

## 📦 Data Model

### ShortUrl (MongoDB Document)

| Field | Description |
|-------|-------------|
| `_id` | Snowflake ID |
| `code` | Unique short code (indexed) |
| `longUrl` | Original URL |
| `createdAt` | Creation timestamp |
| `expiresAt` | TTL index enabled |
| `active` | Active flag |

### ClickEvent (MongoDB Document)

| Field | Description |
|-------|-------------|
| `_id` | Document ID |
| `code` | Short code |
| `timestamp` | Click time |
| `userAgent` | User agent string |
| `ip` | Client IP address |

## ⏳ TTL Auto Expiry

MongoDB TTL index automatically deletes expired URLs when:

- `expiresAt <= current time`
- No scheduler required

## 🚦 Rate Limiting

Redis-based fixed-window rate limiter:

- **Limit:** Configurable (e.g. 100 req/min/IP)
- **Key format:** `rl:{ip}`
- Returns **HTTP 429 Too Many Requests** when exceeded

## 📊 Click Analytics

Every redirect records:

- Short code
- Timestamp
- User-Agent
- IP address

**Stats endpoint:** `GET /api/v1/urls/{code}/stats`

**Response:**

```json
{
  "code": "abc123",
  "clickCount": 42
}
```

## 🚀 API Endpoints

### Create Short URL

`POST /api/v1/urls`

**Request:**

```json
{
  "longUrl": "https://google.com",
  "customAlias": "my-google",
  "expiresAt": "2026-12-31T00:00:00Z"
}
```

**Response:**

```json
{
  "code": "my-google",
  "shortUrl": "http://localhost:8080/my-google"
}
```

### Redirect

`GET /{code}` — Returns HTTP 302 redirect

### Get Stats

`GET /api/v1/urls/{code}/stats`

## 🛠 Tech Stack

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data MongoDB
- Spring Data Redis
- Maven
- Docker (optional)

## 🛣 Roadmap

- 🔐 API key authentication + quotas
- 📊 Aggregated analytics dashboard
- 📦 Docker Compose for full stack
- 📈 Observability (metrics + tracing)
- 🌍 Geo-IP enrichment
- ⚡ Sliding window rate limiter
