package by.fwsys.bot.bot_app.mappers;

import by.fwsys.bot.bot_app.dto.NotificationHistoryDto;
import by.fwsys.bot.bot_app.dto.SendNotificationMessageDto;
import by.fwsys.bot.bot_app.formatters.AbstractNotificationMessageFormatter;
import by.fwsys.bot.bot_app.models.entities.SendNotificationHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class SendNotificationHistoryMapper {

    @Autowired
    private List<AbstractNotificationMessageFormatter<?>> formatters;

    @Mapping(target = "messageShort", source = "messageDto.description")
    @Mapping(target = "messageFull", source = "messageDto", qualifiedByName = "formateMessage")
    public abstract NotificationHistoryDto toDto(SendNotificationHistoryEntity entity);

    @Named("formateMessage")
    protected String formateMessage(SendNotificationMessageDto messageDto) {
        return formatters.stream()
                .filter(f -> f.getType().equals(messageDto.getMessenger()))
                .map(f -> f.getBaseFormattedText(messageDto))
                .findAny()
                .orElse(null);
    }
}
