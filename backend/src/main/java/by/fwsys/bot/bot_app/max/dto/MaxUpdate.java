package by.fwsys.bot.bot_app.max.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MaxUpdate {
    @JsonProperty("update_type")
    private String updateType;
    private MaxMessage message;
    private MaxCallback callback;
    @JsonProperty("chat_id")
    private String chatId;
}
