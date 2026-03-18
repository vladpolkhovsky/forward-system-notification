package by.fwsys.bot.bot_app.services.notifications;

import by.fwsys.bot.bot_app.enums.TargetMessengerType;

public interface NotificationRegistrationService {
    TargetMessengerType getTargetMessenger();
    void register(Long userId, String chatId);
}
