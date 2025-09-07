package dev.esternit.telegram_spring_boot_starter.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity {
    private String type;
    private Integer offset;
    private Integer length;
    private String url;
}
