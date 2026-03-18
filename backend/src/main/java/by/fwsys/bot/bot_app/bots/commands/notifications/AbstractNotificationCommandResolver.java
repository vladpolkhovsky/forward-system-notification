package by.fwsys.bot.bot_app.bots.commands.notifications;

import lombok.RequiredArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public abstract class AbstractNotificationCommandResolver implements NotificationCommandResolver {

    private final Pattern pattern;

    @Override
    public boolean isCommand(String userText) {
        return pattern.matcher(userText).matches();
    }

    protected String getGroup(String userText, String groupName) {
        Matcher matcher = pattern.matcher(userText);
        matcher.find();
        return matcher.group(groupName);
    }
}
