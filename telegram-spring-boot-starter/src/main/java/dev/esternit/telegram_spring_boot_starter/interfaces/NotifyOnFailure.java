package dev.esternit.telegram_spring_boot_starter.interfaces;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotifyOnFailure {

    /**
     * Error message
     */
    String value() default "Method failed with exception";

    /**
     * Should include stack trace
     */
    boolean includeStackTrace() default false;

    /**
     * Exception types to notify
     */
    Class<? extends Throwable>[] notifyOn() default {};

    /**
     * Exception types to ignore
     */
    Class<? extends Throwable>[] ignore() default {};

    /**
     * Error severity
     */
    Severity severity() default Severity.NORMAL;

    /**
     * Chat ID
     */
    String chatId() default "";

    enum Severity {
        LOW, NORMAL, HIGH, CRITICAL
    }
}