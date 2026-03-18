package by.fwsys.bot.bot_app.services.customer;

import by.fwsys.bot.bot_app.dto.SendCustomerMessageDto;
import by.fwsys.bot.bot_app.dto.SendNotificationMessageDto;
import by.fwsys.bot.bot_app.enums.TargetMessengerType;
import by.fwsys.bot.bot_app.formatters.TelegramNotificationMessageFormatter;
import by.fwsys.bot.bot_app.models.entities.CustomerBotEntity;
import by.fwsys.bot.bot_app.models.entities.NotificationBotEntity;
import by.fwsys.bot.bot_app.models.repository.CustomerBotRepository;
import by.fwsys.bot.bot_app.models.repository.NotificationBotRepository;
import by.fwsys.bot.bot_app.services.notifications.NotificationMessageSenderService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@RequiredArgsConstructor
@Service
public class TelegramCustomerNotificationMessageSenderService implements CustomerNotificationMessageSenderService {

    private final TelegramNotificationMessageFormatter telegramNotificationMessageFormatter;
    private final TelegramClient tgCustomerClient;
    private final CustomerBotRepository customerBotRepository;

    @Override
    public TargetMessengerType getTargetMessenger() {
        return TargetMessengerType.TELEGRAM;
    }

    @Override
    @SneakyThrows
    public void send(SendCustomerMessageDto message) {
        log.debug("Preparing to send Telegram notification to forward order: {}", message.getTargetForwardOrderId());

        CustomerBotEntity botEntity = customerBotRepository.findByForwardOrderId(message.getTargetForwardOrderId())
                .orElseThrow(() -> new RuntimeException("Bot not found for forward order: " + message.getTargetForwardOrderId()));

        log.debug("Found bot entity with Telegram chat ID: {}", botEntity.getTgChatId());

        SendMessage sendMessage = telegramNotificationMessageFormatter.format(message);
        sendMessage.setChatId(botEntity.getTgChatId());

        log.info("Executing Telegram send message to chat ID: {}", botEntity.getTgChatId());

        tgCustomerClient.execute(sendMessage);
        
        log.info("Telegram notification sent successfully to chat ID: {}", botEntity.getTgChatId());
    }
}
