package by.fwsys.bot.bot_app.bots.commands.customer;

import by.fwsys.bot.bot_app.enums.TargetMessengerType;
import by.fwsys.bot.bot_app.services.customer.CustomerJoinDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class JoinCustomerCommandResolver extends AbstractCustomerCommandResolver {

    @Autowired
    private CustomerJoinDispatcher customerJoinDispatcher;

    public JoinCustomerCommandResolver() {
        super(Pattern.compile("^\\s*(?<command>/join)\\s*(?<code>\\S{6})\\s*$"));
    }

    @Override
    public boolean processCommand(TargetMessengerType type, String userText, List<Long> attachments, String chatId, String orderId) {
        String code = getGroup(userText, "code");
        return customerJoinDispatcher.register(type, code, chatId);
    }
}
