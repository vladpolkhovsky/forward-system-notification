package by.fwsys.bot.bot_app.mappers;


import by.fwsys.bot.bot_app.dto.NotificationStatusDto;
import by.fwsys.bot.bot_app.models.entities.NotificationBotEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationBotMapper {

    @Mapping(target = "tg", source = "tgChatId", qualifiedByName = "trueIfNotNull")
    @Mapping(target = "vk", source = "vkChatId", qualifiedByName = "trueIfNotNull")
    @Mapping(target = "max", source = "maxChatId", qualifiedByName = "trueIfNotNull")
    NotificationStatusDto toDto(NotificationBotEntity notificationBot);

    @Named("trueIfNotNull")
    default boolean trueIfNotNull(String chatId) {
        return chatId != null;
    }
}
