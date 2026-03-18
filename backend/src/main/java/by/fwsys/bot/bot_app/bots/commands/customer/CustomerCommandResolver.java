package by.fwsys.bot.bot_app.bots.commands.customer;

import by.fwsys.bot.bot_app.enums.TargetMessengerType;

import java.util.List;

public interface CustomerCommandResolver {
    boolean isCommand(String userText);
    boolean processCommand(TargetMessengerType type, String userText, List<Long> attachments, String chatId, String orderId);
}
