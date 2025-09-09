package dev.esternit.telegram_spring_boot_starter.entities;

import lombok.Data;

@Data
public class TelegramResponse<T> {
    private Boolean ok;
    private T result;
    private Integer errorCode;
    private String description;

    public Boolean isOk() {
        return ok != null && ok;
    }
}
