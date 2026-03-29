package by.fwsys.bot.bot_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UiSendNotificationMessageDto {
    private String tittle;
    private String description;
    private String userId;
    private List<String> roles;
}
