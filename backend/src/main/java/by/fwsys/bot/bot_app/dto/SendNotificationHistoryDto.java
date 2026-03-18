package by.fwsys.bot.bot_app.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SendNotificationHistoryDto {
    private Long id;
    private SendNotificationMessageDto messageDto;
    private LocalDateTime createdAt;
}
