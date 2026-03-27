package by.fwsys.bot.bot_app.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserWithStatusDto {
    private Long id;
    private UserDto user;
    private NotificationStatusDto status;
}
