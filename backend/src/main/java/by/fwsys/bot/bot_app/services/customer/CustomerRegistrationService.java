package by.fwsys.bot.bot_app.services.customer;

import by.fwsys.bot.bot_app.enums.TargetMessengerType;

public interface CustomerRegistrationService {
    TargetMessengerType getTargetMessenger();
    void register(String code, String chatId);
}
