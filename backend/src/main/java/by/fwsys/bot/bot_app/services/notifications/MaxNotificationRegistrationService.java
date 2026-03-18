package by.fwsys.bot.bot_app.services.notifications;

import by.fwsys.bot.bot_app.enums.TargetMessengerType;
import by.fwsys.bot.bot_app.models.entities.NotificationBotEntity;
import by.fwsys.bot.bot_app.models.repository.NotificationBotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MaxNotificationRegistrationService implements NotificationRegistrationService{

    private final NotificationBotRepository notificationBotRepository;

    @Override
    public TargetMessengerType getTargetMessenger() {
        return TargetMessengerType.MAX;
    }

    @Override
    public void register(Long userId, String chatId) {
        NotificationBotEntity notificationBotEntity = notificationBotRepository.findById(userId).orElseGet(() -> {
            NotificationBotEntity entity = new NotificationBotEntity();
            entity.setId(userId);
            return entity;
        });

        notificationBotEntity.setMaxChatId(chatId);
        notificationBotRepository.save(notificationBotEntity);
    }
}
