package dev.esternit.telegram_spring_boot_starter.services;

import dev.esternit.telegram_spring_boot_starter.config.TelegramProperties;
import dev.esternit.telegram_spring_boot_starter.entities.SendMessageParams;
import dev.esternit.telegram_spring_boot_starter.entities.TelegramResponse;
import dev.esternit.telegram_spring_boot_starter.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
public class TelegramService {

    private final RestTemplate restTemplate;
    private final String token;
    private final String apiUrl;
    private final String defaultChatId;

    public TelegramService(TelegramProperties properties, RestTemplateBuilder builder) {
        this.token = properties.getToken();
        this.apiUrl = String.format(properties.getApiUrl(), token);
        this.restTemplate = builder.build();
        this.defaultChatId = properties.getErrorChatId();
    }

    /**
     * Send message to chat (<a href="https://core.telegram.org/bots/api#sendmessage">Docs</a>)
     */
    public boolean sendMessage(SendMessageParams params) {
        try {
            if (params.getText() == null || params.getText().isEmpty()) {
                log.warn("Message text is empty or null");
                return false;
            }
            if (params.getText().length() > 4096) {
                log.warn("Message too long ({} chars), truncated", params.getText().length());
                params.setText(params.getText().substring(0, 4096));
            }

            if ("MarkdownV2".equals(params.getParseMode())) {
                params.setText(params.getText());
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            HttpEntity<SendMessageParams> entity = new HttpEntity<>(params, headers);

            ResponseEntity<TelegramResponse<Object>> response = restTemplate.exchange(
                    apiUrl + "/sendMessage",
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<TelegramResponse<Object>>() {}
            );

            boolean success = response.getStatusCode().is2xxSuccessful() && response.getBody() != null && Boolean.TRUE.equals(response.getBody().isOk());

            if (!success) {
                String errorCode = response.getBody() != null ? String.valueOf(response.getBody().getErrorCode()) : "N/A";
                String description = response.getBody() != null ? response.getBody().getDescription() : "No description";
                log.warn("Telegram API error [{}]: {}", errorCode, description);
            }

            return success;

        } catch (Exception e) {
            log.error("Unexpected error while sending Telegram message", e);
            return false;
        }
    }

    /**
     * Low-level API call
     */
    public boolean sendMessage(String chatId, String text, String parseMode) {
        SendMessageParams params = new SendMessageParams();
        params.setChatId(chatId != null ? chatId : defaultChatId);
        params.setText(text);
        params.setParseMode(parseMode);
        return sendMessage(params);
    }

    /**
     * Low-level error API call
     */
    public boolean sendError(String text, String parseMode, String chatId) {
        return sendMessage(chatId != null ? chatId : defaultChatId, text, parseMode);
    }

    /**
     * Get basic information about the bot in form of a User object.
     * See: https://core.telegram.org/bots/api#getme
     */
    public User getMe() {
        try {
            ResponseEntity<TelegramResponse<User>> response = restTemplate.exchange(
                    apiUrl + "/getMe",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<TelegramResponse<User>>() {}
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && Boolean.TRUE.equals(response.getBody().isOk())) {
                return response.getBody().getResult();
            } else {
                String errorCode = response.getBody() != null ? String.valueOf(response.getBody().getErrorCode()) : "N/A";
                String description = response.getBody() != null ? response.getBody().getDescription() : "No description";
                log.warn("Telegram API error [{}]:{}", errorCode, description);
                return null;
            }
        } catch (Exception e) {
            log.error("Unexpected error while calling getMe", e);
            return null;
        }
    }
}