package by.fwsys.bot.bot_app.max.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MaxRecipient {
    @JsonProperty("chat_id")
    private String chatId;
}
