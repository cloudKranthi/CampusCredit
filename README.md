[![Java CI with Docker](https://github.com/cloudKranthi/CampusCredit/actions/workflows/ci.yml/badge.svg)](https://github.com/cloudKranthi/CampusCredit/actions)
CampusCredit-Java Backend Application
CampusCredit is a high-performance, secure digital wallet service designed for campus ecosystems. Built with Java 17 and Spring Boot, it leverages a distributed architecture to ensure transactional integrity, low latency, robust security,concurency and idempotency.
src/main/java/com/wallet
├── config/             # Security & Bean configurations
├── controllers/        # REST Endpoints
├── services/           # Business logic (Transaction handling)
├── filters/            # JWT/Security interceptors
├── exception/          # Global Exception Handling (@ControllerAdvice)
└── repository/         # Postgres Data Access
