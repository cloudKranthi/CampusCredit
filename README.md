CampusCredit-Java Backend Application
[![Java CI with Docker](https://github.com/cloudKranthi/CampusCredit/actions/workflows/ci.yml/badge.svg)](https://github.com/cloudKranthi/CampusCredit/actions)

CampusCredit is an enterprise-grade backend service designed for campus ecosystems where financial integrity is paramount. Built with Java 17 and Spring Boot, the system ensures ACID compliance through atomic transactions and manual rollback mechanisms. By combining Pessimistic Locking with a custom Idempotency Filter, the application guarantees that every credit update is unique, thread-safe, and resilient to network retries or race conditions.


To handle high-concurrency "Money Updates," the service implements a strict validation and locking flow. Below is the conceptual implementation of the transfer logic:
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
public void moneyTransfer(String senderPhone, String receiverPhone, BigDecimal amount, String idempotencyKey, ...) {
    
    // 1. Idempotency Check: Prevent duplicate processing
    if(transactionRepository.findByIdempotencyKey(idempotencyKey).isPresent()) {
        throw new BusinessException("Transaction already processed", HttpStatus.CONFLICT);
    }

    // 2. State Validation: Ensure both Purses are ACTIVE
    Purse senderPurse = purseRepository.findByUserPhone(senderPhone).orElseThrow(...);
    Purse receiverPurse = purseRepository.findByUserPhone(receiverPhone).orElseThrow(...);

    if (isInactive(senderPurse) || isInactive(receiverPurse)) {
        throw new BusinessException("Account is Inactive", HttpStatus.FORBIDDEN);
    }

    // 3. Balance Validation & Atomic Update
    if (senderPurse.getBalance().compareTo(amount) < 0) {
        throw new BusinessException("Insufficient balance", HttpStatus.BAD_REQUEST);
    }

    // 4. Execution: Deduct, Credit, and Log Transaction
    senderPurse.subtract(amount);
    receiverPurse.add(amount);
    
    // 5. Async Messaging: Offload notification to RabbitMQ
    NotificationMessageResponse msg = new NotificationMessageResponse(senderPhone, receiverPhone, amount);
    rabbitTemplate.convertAndSend("notificationExchange", "routing.key", msg);

    // 6. Audit Logging: Record activity for compliance
    auditService.logAudit(senderPhone, "MoneyTransfer", "Success", clientIp);
}

   
src/main/java/com/wallet
├── config/             # Security & Bean configurations
├── controllers/        # REST Endpoints
├── services/           # Business logic (Transaction handling)
├── filters/            # JWT/Security interceptors
├── exception/          # Global Exception Handling (@ControllerAdvice)
└── repository/ # Postgres Data Access
