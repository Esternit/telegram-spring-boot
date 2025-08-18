package dev.esternit.telegram_spring_boot_starter.interfaces;

import java.lang.annotation.*;

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

    enum Severity {
        LOW, NORMAL, HIGH, CRITICAL
    }
}
