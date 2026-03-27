package by.fwsys.bot.bot_app.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationHistoryDto {
    private Long id;
    private String messenger;
    private String messageShort;
    private String messageFull;
    private Long targetUserId;
    private LocalDateTime createdAt;
}
