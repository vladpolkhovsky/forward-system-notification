package by.fwsys.bot.bot_app.services.notifications;

import by.fwsys.bot.bot_app.dto.SendNotificationMessageDto;
import by.fwsys.bot.bot_app.enums.TargetMessengerType;

public interface NotificationMessageSenderService {
    TargetMessengerType getTargetMessenger();
    void send(SendNotificationMessageDto message);
}
