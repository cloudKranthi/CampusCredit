CampusCredit-Java Backend Application
[![Java CI with Docker](https://github.com/cloudKranthi/CampusCredit/actions/workflows/ci.yml/badge.svg)](https://github.com/cloudKranthi/CampusCredit/actions)

CampusCredit is an enterprise-grade backend service designed for campus ecosystems where financial integrity is paramount. Built with Java 17 and Spring Boot, the system ensures ACID compliance through atomic transactions and manual rollback mechanisms. By combining Pessimistic Locking with a custom Idempotency Filter, the application guarantees that every credit update is unique, thread-safe, and resilient to network retries or race conditions.


To handle high-concurrency "Money Updates," the service implements a strict validation and locking flow. Below is the conceptual implementation of the transfer logic:

   
src/main/java/com/wallet
├── config/             # Security & Bean configurations
├── controllers/        # REST Endpoints
├── services/           # Business logic (Transaction handling)
├── filters/            # JWT/Security interceptors
├── exception/          # Global Exception Handling (@ControllerAdvice)
└── repository/         # Postgres Data Access
