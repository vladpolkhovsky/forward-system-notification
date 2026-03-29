package by.fwsys.bot.bot_app.mappers;

import by.fwsys.bot.bot_app.dto.SendNotificationMessageDto;
import by.fwsys.bot.bot_app.dto.UiSendNotificationMessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SendMessageMapper {

    @Mapping(target = "targetUserId", source = "userId")
    @Mapping(target = "tags", expression = "java(java.util.Set.of(\"Администрация\"))")
    @Mapping(target = "messenger", ignore = true)
    @Mapping(target = "informationLevel", constant = "INFO")
    @Mapping(target = "fromUsername", constant = "Администрация")
    SendNotificationMessageDto toDto(UiSendNotificationMessageDto messageDto);
}
