package dev.esternit.telegram_spring_boot_starter.interfaces;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotifyOnFailure {

    /**
     * On failure
     */
    String message() default "Method failed with exception";

    /**
     * Include stack trace
     */
    boolean includeStackTrace() default false;
}
