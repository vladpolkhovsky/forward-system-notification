package by.fwsys.bot.bot_app.services.customer;

import by.fwsys.bot.bot_app.dto.CustomerUserMessageDto;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.regex.Pattern;

@Component
public class CustomerUpdateParser {

    private static final Pattern ORDER_ID_PATTERN = Pattern.compile("(?<orderId>orderId=\\d+)");
    private static final Pattern MESSAGE_ID_PATTERN = Pattern.compile("(?<messageId>messageId=\\d+)");

    public CustomerUserMessageDto parse(Update update) {
        return null;
    }
}
