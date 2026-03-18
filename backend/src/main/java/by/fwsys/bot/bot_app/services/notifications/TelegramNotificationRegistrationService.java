package by.fwsys.bot.bot_app.services.notifications;

import by.fwsys.bot.bot_app.enums.TargetMessengerType;
import by.fwsys.bot.bot_app.models.entities.NotificationBotEntity;
import by.fwsys.bot.bot_app.models.entities.UserEntity;
import by.fwsys.bot.bot_app.models.repository.NotificationBotRepository;
import by.fwsys.bot.bot_app.models.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramNotificationRegistrationService implements NotificationRegistrationService{

    private final NotificationBotRepository notificationBotRepository;
    private final UserRepository userRepository;

    @Override
    public TargetMessengerType getTargetMessenger() {
        return TargetMessengerType.TELEGRAM;
    }

    @Override
    public void register(Long userId, String chatId) {
        NotificationBotEntity notificationBotEntity = notificationBotRepository.findById(userId).orElseGet(() -> {
            NotificationBotEntity entity = new NotificationBotEntity();
            UserEntity user = userRepository.findById(userId).orElseThrow();
            entity.setUser(user);
            return entity;
        });

        notificationBotEntity.setTgChatId(chatId);
        notificationBotRepository.save(notificationBotEntity);
    }
}
