package dev.esternit.telegram_spring_boot_starter.services;

import dev.esternit.telegram_spring_boot_starter.interfaces.NotifyOnFailure;
import dev.esternit.telegram_spring_boot_starter.interfaces.NotifyOnSuccess;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
public class AsyncTelegramNotifier {

    private final TelegramService telegramService;

    @Async("telegramNotificationExecutor")
    public void sendFailureNotification(String message, NotifyOnFailure.Severity severity, String chatId) {
        boolean sent = telegramService.sendError(message, "MarkdownV2", chatId);
        if (!sent) {
            log.error("Failed to send FAILURE notification. Severity: {}", severity);
        }
    }

    @Async("telegramNotificationExecutor")
    public void sendSuccessNotification(String message, NotifyOnSuccess.Severity severity, String chatId) {
        boolean sent = telegramService.sendMessage(chatId, message, "MarkdownV2");
        if (!sent) {
            log.error("Failed to send SUCCESS notification. Severity: {}", severity);
        }
    }
}
