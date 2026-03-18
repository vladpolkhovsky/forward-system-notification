package by.fwsys.bot.bot_app.bots.commands.notifications;

import by.fwsys.bot.bot_app.enums.TargetMessengerType;
import by.fwsys.bot.bot_app.services.notifications.NotificationRegistrationDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class RegisterNotificationCommandResolver extends AbstractNotificationCommandResolver{
    
    @Autowired
    private NotificationRegistrationDispatcher registrationDispatcher;
    
    public RegisterNotificationCommandResolver() {
        super(Pattern.compile("^\\s*(?<command>/register|/reg)\\s*(?<code>\\S{6})\\s*$"));
    }

    @Override
    public boolean processCommand(TargetMessengerType type, String userText, String chatId) {
        String code = getGroup(userText, "code");
        return registrationDispatcher.register(type, code, chatId);
    }
}
