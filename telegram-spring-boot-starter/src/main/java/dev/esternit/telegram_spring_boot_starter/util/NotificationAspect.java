package dev.esternit.telegram_spring_boot_starter.util;

import dev.esternit.telegram_spring_boot_starter.interfaces.NotifyOnFailure;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Aspect
public class NotificationAspect {

    private static final Logger log = LoggerFactory.getLogger(NotificationAspect.class);

    private final TelegramService telegramService;

    @Autowired
    public NotificationAspect(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @AfterThrowing(
            pointcut = "execution(public * *(..)) && @annotation(notifyOnFailure)",
            throwing = "exception"
    )
    public void notifyOnFailure(NotifyOnFailure notifyOnFailure, Throwable exception) {
        String message = notifyOnFailure.message();

        StringBuilder fullMessage = new StringBuilder();
        fullMessage.append("🚨 *Error*\n");
        fullMessage.append("*Message:* ").append(escapeMarkdown(message)).append("\n");
        fullMessage.append("*Exception:* `").append(exception.getClass().getSimpleName())
                .append(": ").append(escapeMarkdown(exception.getMessage())).append("`\n");

        if (notifyOnFailure.includeStackTrace()) {
            fullMessage.append("*Stack trace:*\n```\n");
            for (StackTraceElement element : exception.getStackTrace()) {
                fullMessage.append(escapeMarkdown(element.toString())).append("\n");
            }
            fullMessage.append("```\n");
        }
        log.info(fullMessage.toString());

        boolean sent = telegramService.sendError(fullMessage.toString(), "MarkdownV2");
        if (sent) {
            log.info("Notification sent");
        } else {
            log.error("Failed to send notification");
        }
    }

    private String escapeMarkdown(String text) {
        if (text == null) return "null";
        return text.replaceAll("[_\\*\\[\\]\\(\\)~`>#+\\-=|{}.!]", "\\\\$0");
    }
}
