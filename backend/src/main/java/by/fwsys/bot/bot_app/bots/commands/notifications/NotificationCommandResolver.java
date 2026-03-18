package by.fwsys.bot.bot_app.bots.commands.notifications;

import by.fwsys.bot.bot_app.enums.TargetMessengerType;

public interface NotificationCommandResolver {
    boolean isCommand(String userText);
    boolean processCommand(TargetMessengerType type, String userText, String chatId);
}
