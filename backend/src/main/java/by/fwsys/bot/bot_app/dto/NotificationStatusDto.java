package by.fwsys.bot.bot_app.dto;

import lombok.Data;

@Data
public class NotificationStatusDto {
    private Long id;
    private boolean tg;
    private boolean vk;
    private boolean max;

    public static NotificationStatusDto allOff(Long id) {
        NotificationStatusDto notificationStatusDto = new NotificationStatusDto();
        notificationStatusDto.setId(id);
        return notificationStatusDto;
    }
}
