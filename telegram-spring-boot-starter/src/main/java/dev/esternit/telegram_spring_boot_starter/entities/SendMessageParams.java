package dev.esternit.telegram_spring_boot_starter.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageParams {

    private String chatId;
    private String text;
    private String parseMode;
    private Boolean disableNotification;
    private Boolean protectContent;
    private Boolean allowPaidBroadcast;
    private String messageThreadId;
    private String replyToMessageId;
    private Boolean allowSendingWithoutReply;
    private Object replyMarkup;
    private LinkPreviewOptions linkPreviewOptions;
    private List<MessageEntity> entities;
    private Integer messageEffectId;
    private String businessConnectionId;

    @JsonProperty("chat_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getChatId() { return chatId; }

    @JsonProperty("parse_mode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getParseMode() { return parseMode; }

    @JsonProperty("disable_notification")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Boolean getDisableNotification() { return disableNotification; }

    @JsonProperty("protect_content")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Boolean getProtectContent() { return protectContent; }

    @JsonProperty("allow_paid_broadcast")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Boolean getAllowPaidBroadcast() { return allowPaidBroadcast; }

    @JsonProperty("message_thread_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getMessageThreadId() { return messageThreadId; }

    @JsonProperty("reply_to_message_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getReplyToMessageId() { return replyToMessageId; }

    @JsonProperty("allow_sending_without_reply")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Boolean getAllowSendingWithoutReply() { return allowSendingWithoutReply; }

    @JsonProperty("reply_markup")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Object getReplyMarkup() { return replyMarkup; }

    @JsonProperty("link_preview_options")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public LinkPreviewOptions getLinkPreviewOptions() { return linkPreviewOptions; }

    @JsonProperty("entities")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<MessageEntity> getEntities() { return entities; }

    @JsonProperty("message_effect_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer getMessageEffectId() { return messageEffectId; }

    @JsonProperty("business_connection_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getBusinessConnectionId() { return businessConnectionId; }
}