package by.fwsys.bot.bot_app.services.customer;

import by.fwsys.bot.bot_app.dto.SendCustomerMessageDto;
import by.fwsys.bot.bot_app.dto.SendNotificationMessageDto;
import by.fwsys.bot.bot_app.enums.TargetMessengerType;

public interface CustomerNotificationMessageSenderService {
    TargetMessengerType getTargetMessenger();
    void send(SendCustomerMessageDto message);
}
