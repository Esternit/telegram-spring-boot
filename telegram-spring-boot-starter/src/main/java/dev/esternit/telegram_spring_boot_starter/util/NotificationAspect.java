package dev.esternit.telegram_spring_boot_starter.util;

import dev.esternit.telegram_spring_boot_starter.interfaces.NotifyOnFailure;
import dev.esternit.telegram_spring_boot_starter.interfaces.NotifyOnSuccess;
import dev.esternit.telegram_spring_boot_starter.services.AsyncTelegramNotifier;
import dev.esternit.telegram_spring_boot_starter.services.TelegramService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Arrays;

@Aspect
@Slf4j
public class NotificationAspect {

    private final NotificationThrottler throttler;
    private final AsyncTelegramNotifier asyncNotifier;

    public NotificationAspect(NotificationThrottler throttler,
                              AsyncTelegramNotifier asyncNotifier) {
        this.throttler = throttler;
        this.asyncNotifier = asyncNotifier;
    }

    @Around("@annotation(notifyOnFailure)")
    public Object notifyOnFailure(ProceedingJoinPoint joinPoint, NotifyOnFailure notifyOnFailure) throws Throwable {
        log.debug("🔔 AOP: Entering @NotifyOnFailure for {}", joinPoint.getSignature().toShortString());
        String methodKey = joinPoint.getSignature().toShortString();

        if (!throttler.canNotify(methodKey, notifyOnFailure.cooldownSeconds())) {
            log.debug("🔔 AOP: failure throttling for {}", methodKey);
            return joinPoint.proceed();
        }


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

            asyncNotifier.sendFailureNotification(fullMessage, notifyOnFailure.severity(), notifyOnFailure.chatId());
            throw ex;
        }
    }

    @AfterReturning(
            pointcut = "@annotation(notifyOnSuccess)",
            returning = "result"
    )
    public void notifyOnSuccess(JoinPoint joinPoint, NotifyOnSuccess notifyOnSuccess, Object result) {
        String methodKey = joinPoint.getSignature().toShortString();

        if (!throttler.canNotify(methodKey, notifyOnSuccess.cooldownSeconds())) {
            log.debug("🔔 AOP: success throttling for {}", methodKey);
            return;
        }

        String method = joinPoint.getSignature().toShortString();
        String message = notifyOnSuccess.value().isEmpty() ? "Method succeeded" : notifyOnSuccess.value();

        String fullMessage = NotificationMessageBuilder.buildSuccessMessage(
                method,
                message,
                notifyOnSuccess.includeResult() ? result : null
        );

        asyncNotifier.sendSuccessNotification(fullMessage, notifyOnSuccess.severity(), notifyOnSuccess.chatId());
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
}
