package by.fwsys.bot.bot_app.services.notifications;

import by.fwsys.bot.bot_app.dto.SendNotificationMessageDto;
import by.fwsys.bot.bot_app.enums.TargetMessengerType;
import by.fwsys.bot.bot_app.formatters.VkNotificationMessageFormatter;
import by.fwsys.bot.bot_app.models.entities.NotificationBotEntity;
import by.fwsys.bot.bot_app.models.repository.NotificationBotRepository;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VkNotificationMessageSenderService implements NotificationMessageSenderService {

    private final NotificationBotRepository notificationBotRepository;
    private final VkApiClient vkNotificationClient;
    private final GroupActor vkNotificationGroupActor;
    private final VkNotificationMessageFormatter formatter;

    @Override

    public TargetMessengerType getTargetMessenger() {
        return TargetMessengerType.VK;
    }

    @Override
    @SneakyThrows
    public void send(SendNotificationMessageDto message) {
        log.debug("Preparing to send Vk notification to user ID: {}", message.getTargetUserId());

        NotificationBotEntity botEntity = notificationBotRepository.findById(message.getTargetUserId())
                .orElseThrow(() -> new RuntimeException("Bot not found for user ID: " + message.getTargetUserId()));

        log.debug("Found bot entity with Vk chat ID: {}", botEntity.getTgChatId());

        vkNotificationClient.messages().sendUserIds(vkNotificationGroupActor)
                .userId(Long.parseLong(botEntity.getVkChatId()))
                .randomId(0)
                .message(formatter.format(message))
                .executeAsString();
    }
}
