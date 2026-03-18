package by.fwsys.bot.bot_app.dto;

import by.fwsys.bot.bot_app.enums.InformationLevel;
import by.fwsys.bot.bot_app.enums.TargetMessengerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendNotificationMessageDto implements Serializable {
    @With
    private TargetMessengerType messenger;
    private InformationLevel informationLevel;
    private String tittle;
    private String description;
    private String fromUsername;
    private Long targetUserId;
    private Set<String> tags;
}