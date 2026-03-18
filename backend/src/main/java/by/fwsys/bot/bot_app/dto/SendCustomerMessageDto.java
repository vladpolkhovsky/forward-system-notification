package by.fwsys.bot.bot_app.dto;

import by.fwsys.bot.bot_app.enums.InformationLevel;
import by.fwsys.bot.bot_app.enums.TargetMessengerType;
import lombok.Data;

import java.util.Set;

@Data
public class SendCustomerMessageDto {
    private InformationLevel informationLevel;
    private String tittle;
    private String description;
    private String fromUsername;
    private Long targetForwardOrderId;
    private Set<String> tags;
    private Set<String> links;
}