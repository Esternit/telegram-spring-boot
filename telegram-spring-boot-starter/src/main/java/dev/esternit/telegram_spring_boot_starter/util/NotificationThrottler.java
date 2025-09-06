package dev.esternit.telegram_spring_boot_starter.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class NotificationThrottler {

    private final Map<String, Long> lastNotificationTime = new ConcurrentHashMap<>();

    /**
     * Check if it's possible to send notification with the given key
     * @param key unique key (method name, "MyService.myMethod")
     * @param cooldownSeconds minimum cooldown in seconds
     * @return true, if notification can be sent
     */
    public boolean canNotify(String key, int cooldownSeconds) {
        long now = System.currentTimeMillis();
        long lastTime = lastNotificationTime.getOrDefault(key, 0L);

        if (now - lastTime < cooldownSeconds * 1000L) {
            return false; // еще в cooldown
        }

        lastNotificationTime.put(key, now);
        return true;
    }
}