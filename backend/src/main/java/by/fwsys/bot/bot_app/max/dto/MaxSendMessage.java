package by.fwsys.bot.bot_app.max.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaxSendMessage {
    private String chatId;
    private String text;
}
