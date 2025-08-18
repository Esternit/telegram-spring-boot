package dev.esternit.telegram_spring_boot_starter.util;

import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;

@Component
@UtilityClass
public class NotificationMessageBuilder {
    public static String escapeMarkdown(String text) {
        if (text == null) return "null";
        return text.replaceAll("[_\\*\\[\\]\\(\\)~`>#+\\-=|{}.!]", "\\\\$0");
    }

    public static String buildFailureMessage(
            String method,
            String message,
            Throwable exception,
            boolean includeStackTrace) {

        StringBuilder sb = new StringBuilder();
        sb.append("🚨 *Error*\n");
        sb.append("*Method:* ").append(escapeMarkdown(method)).append("\n");
        sb.append("*Message:* ").append(escapeMarkdown(message)).append("\n");
        sb.append("*Exception:* `")
                .append(exception.getClass().getSimpleName())
                .append(": ")
                .append(escapeMarkdown(exception.getMessage()))
                .append("`\n");

        if (includeStackTrace) {
            sb.append("*Stack trace:*\n```\n");
            for (StackTraceElement element : exception.getStackTrace()) {
                sb.append(escapeMarkdown(element.toString())).append("\n");
            }
            sb.append("```\n");
        }
        return sb.toString();
    }

    public static String buildSuccessMessage(
            String method,
            String message,
            Object result) {

        StringBuilder sb = new StringBuilder();
        sb.append("✅ *Success*\n");
        sb.append("*Method:* ").append(escapeMarkdown(method)).append("\n");
        sb.append("*Message:* ").append(escapeMarkdown(message)).append("\n");
        if (result != null) {
            sb.append("*Result:* `").append(escapeMarkdown(result.toString())).append("`\n");
        }
        return sb.toString();
    }
}
