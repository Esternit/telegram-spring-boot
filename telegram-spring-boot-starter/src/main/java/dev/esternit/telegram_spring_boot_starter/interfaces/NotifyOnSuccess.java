package dev.esternit.telegram_spring_boot_starter.interfaces;

import java.lang.annotation.*;

/**
 * @author Esternit
 * @since 0.1.0
 * Annotation to notify on success
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotifyOnSuccess {

    /**
     * Message
     */
    String value() default "Method completed successfully";

    /**
     * Should include result
     */
    boolean includeResult() default false;

    /**
     * Error severity
     */
    Severity severity() default Severity.NORMAL;

    /**
     * Chat ID
     */
    String chatId() default "";

    /**
     * Cooldown in seconds
     */
    int cooldownSeconds() default 60;

    enum Severity {
        LOW, NORMAL, HIGH, CRITICAL
    }
}
