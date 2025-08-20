# Module Telegram Spring Boot Starter

# Telegram Spring Boot Starter

Lightweight Telegram Bot API starter for Spring Boot (HTTP-based, no SDK).

## Описание

This library allows you to easily integrate a Telegram bot into a Spring Boot application without using third-party SDKs.

Supports:
- Automatic message sending
- Asynchronous sending
- Error handling via annotations
- Configuration via `application.yml`

## Пример использования

```java
@NotifyOnFailure(
    value = "Failed to send notification",
    includeStackTrace = true,
    severity = NotifyOnFailure.Severity.HIGH,
    chatId = "${telegram.admin.chatId}"
)
public void sendAlert(String message) {
    // ...
}
```