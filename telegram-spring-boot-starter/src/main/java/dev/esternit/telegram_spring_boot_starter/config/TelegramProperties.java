package dev.esternit.telegram_spring_boot_starter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "telegram.bot")
@Getter
@Setter
public class TelegramProperties {

    /**
     * Bot token (can be obtained from @BotFather)
     */
    private String token;

    /**
     * Base URL for Telegram API
     */
    private String apiUrl = "https://api.telegram.org/bot%s";

    /**
     * Chat id for errors
     */
    private String errorChatId;

}
