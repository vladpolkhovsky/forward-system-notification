package by.fwsys.bot.bot_app.mappers;

import by.fwsys.bot.bot_app.dto.GoogleDocxSyncDto;
import by.fwsys.bot.bot_app.models.entities.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GoogleDocxSyncMapper {

    @Mapping(target = "status", source = "orderStatus.rusName")
    @Mapping(target = "authorDeadline", source = "intermediateDeadline", dateFormat = "dd.MM")
    @Mapping(target = "deadline", source = "deadline", dateFormat = "dd.MM")
    @Mapping(target = "techNumber", expression = "java('№' + order.getTechNumber())")
    GoogleDocxSyncDto toDto(OrderEntity order);
}
