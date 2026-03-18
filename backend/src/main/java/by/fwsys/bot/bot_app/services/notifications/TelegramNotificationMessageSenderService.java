package by.fwsys.bot.bot_app.services.notifications;

import by.fwsys.bot.bot_app.dto.SendNotificationMessageDto;
import by.fwsys.bot.bot_app.enums.TargetMessengerType;
import by.fwsys.bot.bot_app.formatters.TelegramNotificationMessageFormatter;
import by.fwsys.bot.bot_app.models.entities.NotificationBotEntity;
import by.fwsys.bot.bot_app.models.repository.NotificationBotRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramNotificationMessageSenderService implements NotificationMessageSenderService {

    private final TelegramNotificationMessageFormatter telegramNotificationMessageFormatter;
    private final TelegramClient tgNotificationClient;
    private final NotificationBotRepository notificationBotRepository;

    @Override
    public TargetMessengerType getTargetMessenger() {
        return TargetMessengerType.TELEGRAM;
    }

    @Override
    @SneakyThrows
    public void send(SendNotificationMessageDto message) {
        log.debug("Preparing to send Telegram notification to user ID: {}", message.getTargetUserId());
        
        NotificationBotEntity botEntity = notificationBotRepository.findById(message.getTargetUserId())
                .orElseThrow(() -> new RuntimeException("Bot not found for user ID: " + message.getTargetUserId()));

        log.debug("Found bot entity with Telegram chat ID: {}", botEntity.getTgChatId());

        SendMessage sendMessage = telegramNotificationMessageFormatter.format(message);
        sendMessage.setChatId(botEntity.getTgChatId());

        log.info("Executing Telegram send message to chat ID: {}", botEntity.getTgChatId());

        tgNotificationClient.execute(sendMessage);
        
        log.info("Telegram notification sent successfully to chat ID: {}", botEntity.getTgChatId());
    }
}
