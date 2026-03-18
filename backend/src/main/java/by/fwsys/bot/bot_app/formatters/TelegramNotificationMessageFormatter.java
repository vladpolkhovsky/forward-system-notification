package by.fwsys.bot.bot_app.formatters;

import by.fwsys.bot.bot_app.dto.SendCustomerMessageDto;
import by.fwsys.bot.bot_app.dto.SendNotificationMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
public class TelegramNotificationMessageFormatter extends AbstractNotificationMessageFormatter<SendMessage> {

    @Override
    public SendMessage format(SendNotificationMessageDto messageDto) {
        String text = getBaseFormattedText(messageDto);
        return SendMessage.builder()
                .text(text)
                .chatId("---")
                .parseMode("MarkdownV2")
                .build();
    }

    @Override
    public SendMessage format(SendCustomerMessageDto messageDto) {
        String text = escapeMarkdownV2(getBaseFormattedText(messageDto));
        return SendMessage.builder()
                .text(text)
                .chatId("---")
                .parseMode("MarkdownV2")
                .build();
    }
}
