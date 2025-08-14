package dev.esternit.telegram_spring_boot_starter.util;

import dev.esternit.telegram_spring_boot_starter.interfaces.NotifyOnFailure;
import dev.esternit.telegram_spring_boot_starter.interfaces.NotifyOnSuccess;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

@Aspect
@Slf4j
public class NotificationAspect {

    private final TelegramService telegramService;

    public NotificationAspect(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @Around("@annotation(notifyOnFailure)")
    public Object notifyOnFailure(ProceedingJoinPoint joinPoint, NotifyOnFailure notifyOnFailure) throws Throwable {
        log.debug("🔔 AOP: Entering @NotifyOnFailure for {}", joinPoint.getSignature().toShortString());

        try {
            return joinPoint.proceed();
        } catch (Throwable ex) {
            if (!shouldNotifyException(ex, notifyOnFailure)) {
                throw ex;
            }

            String method = joinPoint.getSignature().toShortString();
            String message = notifyOnFailure.value().isEmpty() ? "Method failed" : notifyOnFailure.value();

            String fullMessage = NotificationMessageBuilder.buildFailureMessage(
                    method,
                    message,
                    ex,
                    notifyOnFailure.includeStackTrace()
            );

            sendMessage(fullMessage, notifyOnFailure.severity(), notifyOnFailure.chatId());
            throw ex;
        }
    }

    @AfterReturning(
            pointcut = "@annotation(notifyOnSuccess)",
            returning = "result"
    )
    public void notifyOnSuccess(JoinPoint joinPoint, NotifyOnSuccess notifyOnSuccess, Object result) {
        String method = joinPoint.getSignature().toShortString();
        String message = notifyOnSuccess.value().isEmpty() ? "Method succeeded" : notifyOnSuccess.value();

        String fullMessage = NotificationMessageBuilder.buildSuccessMessage(
                method,
                message,
                notifyOnSuccess.includeResult() ? result : null
        );

        sendMessage(fullMessage, notifyOnSuccess.severity(), notifyOnSuccess.chatId());
    }

    private boolean shouldNotifyException(Throwable ex, NotifyOnFailure notify) {
        Class<? extends Throwable>[] notifyOn = notify.notifyOn();
        if (notifyOn.length > 0) {
            return Arrays.stream(notifyOn)
                    .anyMatch(clazz -> clazz.isAssignableFrom(ex.getClass()));
        }

        Class<? extends Throwable>[] ignore = notify.ignore();
        return Arrays.stream(ignore)
                .noneMatch(clazz -> clazz.isAssignableFrom(ex.getClass()));
    }

    private void sendMessage(String message, NotifyOnFailure.Severity severity, String chatId) {
        boolean sent = telegramService.sendError(message, "MarkdownV2", chatId);
        if (!sent) {
            log.error("Failed to send failure notification. Severity: {}", severity);
        }
    }

    private void sendMessage(String message, NotifyOnSuccess.Severity severity, String chatId) {
        boolean sent = telegramService.sendError(message, "MarkdownV2", chatId);
        if (!sent) {
            log.error("Failed to send success notification. Severity: {}", severity);
        }
    }
}
