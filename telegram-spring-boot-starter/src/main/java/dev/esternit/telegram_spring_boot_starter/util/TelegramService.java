package dev.esternit.telegram_spring_boot_starter.util;

import dev.esternit.telegram_spring_boot_starter.config.TelegramProperties;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Objects;

@Data
public class TelegramService {

    private static final Logger log = LoggerFactory.getLogger(TelegramService.class);

    private final RestTemplate restTemplate;
    private final String token;
    private final String apiUrl;
    private final String errorChatId;

    public TelegramService(TelegramProperties properties, RestTemplateBuilder builder) {
        this.token = properties.getToken();
        this.apiUrl = String.format(properties.getApiUrl(), token);
        this.restTemplate = builder.build();
        this.errorChatId = properties.getErrorChatId();
    }

    /**
     * Send error message to Telegram
     *
     * @param text message
     * @return true if message was sent
     */
    public boolean sendError(String text, String parseMode, String chatId) {
        return sendMessage(chatId == null ? errorChatId : chatId, text, parseMode);
    }

    /**
     * Send message to Telegram
     *
     * @param chatId chat id
     * @param text   message
     * @return true if message was sent
     */
    public boolean sendMessage(String chatId, String text, String parseMode) {
        try {
            SendMessageRequest request = new SendMessageRequest();
            request.chat_id = chatId;
            request.text = text;
            if (parseMode != null) {
                request.parse_mode = parseMode;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<SendMessageRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<TelegramResponse> response = restTemplate.postForEntity(
                    apiUrl,
                    entity,
                    TelegramResponse.class
            );

            boolean success = response.getStatusCode() == HttpStatus.OK && Boolean.TRUE.equals(Objects.requireNonNull(response.getBody()).ok);
            if (!success) {
                log.warn("Telegram API error: {}", response.getBody());
            }
            return success;
        } catch (Exception e) {
            log.error("Failed to send message to Telegram");
            return false;
        }
    }

    @Data
    private static class SendMessageRequest {
        public String chat_id;
        public String text;
        public String parse_mode;
    }

    @Data
    private static class TelegramResponse {
        public Boolean ok;
        public Integer error_code;
        public String description;
    }
}