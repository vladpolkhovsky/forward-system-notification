package by.fwsys.bot.bot_app.services.notifications;

import by.fwsys.bot.bot_app.dto.SendNotificationMessageDto;
import by.fwsys.bot.bot_app.dto.UiSendNotificationMessageDto;
import by.fwsys.bot.bot_app.enums.TargetMessengerType;
import by.fwsys.bot.bot_app.mappers.SendMessageMapper;
import by.fwsys.bot.bot_app.models.entities.NotificationBotEntity;
import by.fwsys.bot.bot_app.models.entities.SendNotificationHistoryEntity;
import by.fwsys.bot.bot_app.models.entities.UserEntity;
import by.fwsys.bot.bot_app.models.repository.NotificationBotRepository;
import by.fwsys.bot.bot_app.models.repository.SendNotificationHistoryRepository;
import by.fwsys.bot.bot_app.models.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationDispatcher {

    private final List<NotificationMessageSenderService> senders;
    private final NotificationBotRepository notificationBotRepository;
    private final SendNotificationHistoryRepository sendNotificationHistoryRepository;
    private final SendMessageMapper sendMessageMapper;
    private final UserRepository userRepository;

    @SneakyThrows
    @Transactional
    public void dispatch(SendNotificationMessageDto messageDto) {
        log.debug("Dispatching notification to messenger: {}", messageDto.getMessenger());

        NotificationMessageSenderService notificationMessageSenderService = senders.stream()
                .filter(sender -> sender.getTargetMessenger().equals(messageDto.getMessenger()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No sender found for messenger: " + messageDto.getMessenger()));

        log.info("Sending notification via {}: title='{}'", messageDto.getMessenger(), messageDto.getTittle());
        notificationMessageSenderService.send(messageDto);

        saveHistory(messageDto);

        log.info("Notification sent and saved to history");
    }

    @SneakyThrows
    @Transactional
    public void dispatchAll(SendNotificationMessageDto messageDto) {
        try {
            log.debug("Dispatching broadcast notification to user ID: {}", messageDto.getTargetUserId());

            NotificationBotEntity notificationBot = notificationBotRepository.findById(messageDto.getTargetUserId())
                    .orElseThrow(() -> new RuntimeException("Bot not found for user ID: " + messageDto.getTargetUserId()));

            if (notificationBot.getTgChatId() != null)
                send(TargetMessengerType.TELEGRAM, messageDto.withMessenger(TargetMessengerType.TELEGRAM));

            if (notificationBot.getMaxChatId() != null)
                send(TargetMessengerType.MAX, messageDto.withMessenger(TargetMessengerType.MAX));

            if (notificationBot.getVkChatId() != null)
                send(TargetMessengerType.VK, messageDto.withMessenger(TargetMessengerType.VK));

            log.info("Broadcast notification sent and saved to history");
        } catch (Exception ex) {
            log.error("Cant dispatch", ex);
        }
    }

    private void saveHistory(SendNotificationMessageDto messageDto) {
        SendNotificationHistoryEntity history = new SendNotificationHistoryEntity();
        history.setMessenger(messageDto.getMessenger());
        history.setTargetUserId(messageDto.getTargetUserId());
        history.setMessageDto(messageDto);
        sendNotificationHistoryRepository.save(history);
        log.debug("Notification history saved");
    }

    private void send(TargetMessengerType type, SendNotificationMessageDto messageDto) {
        try {
            log.info("Sending broadcast notification via {}: title='{}'", messageDto.getMessenger(), messageDto.getTittle());
            senders.stream().filter(sender -> Objects.equals(type, sender.getTargetMessenger())).findAny()
                    .ifPresent(sender -> sender.send(messageDto));
            saveHistory(messageDto);
        } catch (Exception ex) {
            log.error("Cant send message to {} for order {}", type, messageDto.getMessenger(), ex);
        }
    }

    @SneakyThrows
    @Transactional
    public void sendMessageUi(UiSendNotificationMessageDto messageDto) {
        SendNotificationMessageDto dto = sendMessageMapper.toDto(messageDto);

        if (dto.getTargetUserId() != null) {
            dispatchAll(dto);
        }

        messageDto.getRoles().forEach(role -> {
            userRepository.findByRole(role).forEach(user -> {
                dto.setTargetUserId(user.getId());
                dispatchAll(dto);
            });
        });
    }
}
