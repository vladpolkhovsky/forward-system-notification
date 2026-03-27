package by.fwsys.bot.bot_app.formatters;

import by.fwsys.bot.bot_app.dto.SendCustomerMessageDto;
import by.fwsys.bot.bot_app.dto.SendNotificationMessageDto;
import by.fwsys.bot.bot_app.enums.TargetMessengerType;

public interface NotificationMessageFormatter<T> {
    T format(SendNotificationMessageDto messageDto);
    T format(SendCustomerMessageDto messageDto);
    TargetMessengerType getType();
}
